import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Biblioteca: mantém catálogo de livros e lista de alunos.
 * Persiste dados em ficheiros de texto simples (livros.txt e alunos.txt).
 */
public class Biblioteca {

    private List<Livro>  catalogo  = new ArrayList<>();
    private List<Aluno>  alunos    = new ArrayList<>();

    private static final String FICHEIRO_LIVROS = "livros.txt";
    private static final String FICHEIRO_ALUNOS = "alunos.txt";

    // ------------------------------------------------------------------ //
    //  GESTÃO DE LIVROS
    // ------------------------------------------------------------------ //

    public void adicionarLivro(Livro livro) {
        if (livro == null) throw new IllegalArgumentException("Livro nulo");
        catalogo.add(livro);
        System.out.println("Livro adicionado: " + livro.info());
    }

    public boolean removerLivro(String isbn) {
        Livro l = buscarPorIsbn(isbn);
        if (l == null) return false;
        catalogo.remove(l);
        return true;
    }

    public Livro buscarPorIsbn(String isbn) {
        for (Livro l : catalogo)
            if (l.getIsbn().equals(isbn)) return l;
        return null;
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro l : catalogo)
            if (l.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                resultado.add(l);
        return resultado;
    }

    public List<Livro> buscarPorAutor(String autor) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro l : catalogo)
            if (l.getAutor().toLowerCase().contains(autor.toLowerCase()))
                resultado.add(l);
        return resultado;
    }

    public List<Livro> getCatalogo() { return new ArrayList<>(catalogo); }

    // ------------------------------------------------------------------ //
    //  GESTÃO DE ALUNOS
    // ------------------------------------------------------------------ //

    public void adicionarAluno(Aluno aluno) {
        if (aluno == null) throw new IllegalArgumentException("Aluno nulo");
        alunos.add(aluno);
        System.out.println("Aluno registado: " + aluno);
    }

    public Aluno buscarAlunoPorMatricula(String matricula) {
        for (Aluno a : alunos)
            if (a.getNumeroMatricula().equals(matricula)) return a;
        return null;
    }

    public List<Aluno> getAlunos() { return new ArrayList<>(alunos); }

    // ------------------------------------------------------------------ //
    //  EMPRÉSTIMOS
    // ------------------------------------------------------------------ //

    public boolean emprestarLivro(String matricula, String isbn) {
        Aluno aluno = buscarAlunoPorMatricula(matricula);
        if (aluno == null) {
            System.out.println("Aluno nao encontrado: " + matricula);
            return false;
        }
        Livro livro = buscarPorIsbn(isbn);
        if (livro == null) {
            System.out.println("Livro nao encontrado: " + isbn);
            return false;
        }
        boolean ok = aluno.matricularLivro(livro);
        if (ok)
            System.out.println("Emprestimo realizado: " + aluno.getNome() + " -> " + livro.getTitulo());
        else
            System.out.println("Livro indisponivel: " + livro.getTitulo());
        return ok;
    }

    public boolean devolverLivro(String matricula, String isbn) {
        Aluno aluno = buscarAlunoPorMatricula(matricula);
        if (aluno == null) {
            System.out.println("Aluno nao encontrado: " + matricula);
            return false;
        }
        Livro livro = buscarPorIsbn(isbn);
        if (livro == null) {
            System.out.println("Livro nao encontrado: " + isbn);
            return false;
        }
        boolean ok = aluno.devolverLivro(livro);
        if (ok)
            System.out.println("Devolucao realizada: " + aluno.getNome() + " -> " + livro.getTitulo());
        else
            System.out.println("Este aluno nao tem este livro emprestado.");
        return ok;
    }

    // ------------------------------------------------------------------ //
    //  PERSISTÊNCIA EM FICHEIROS DE TEXTO
    // ------------------------------------------------------------------ //

    /** Guarda todo o catálogo de livros em livros.txt */
    public void guardarLivros() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHEIRO_LIVROS))) {
            for (Livro l : catalogo)
                pw.println(l.paraTexto());
            System.out.println("Livros guardados em " + FICHEIRO_LIVROS + " (" + catalogo.size() + " registos).");
        } catch (IOException e) {
            System.out.println("Erro ao guardar livros: " + e.getMessage());
        }
    }

    /** Carrega catálogo de livros a partir de livros.txt */
    public void carregarLivros() {
        File f = new File(FICHEIRO_LIVROS);
        if (!f.exists()) {
            System.out.println("Ficheiro " + FICHEIRO_LIVROS + " nao encontrado. Iniciando com catalogo vazio.");
            return;
        }
        catalogo.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.isBlank()) {
                    try {
                        catalogo.add(Livro.deTexto(linha));
                    } catch (Exception e) {
                        System.out.println("Linha invalida ignorada: " + linha);
                    }
                }
            }
            System.out.println("Livros carregados: " + catalogo.size());
        } catch (IOException e) {
            System.out.println("Erro ao carregar livros: " + e.getMessage());
        }
    }

    /** Guarda todos os alunos em alunos.txt (inclui lista de ISBNs emprestados) */
    public void guardarAlunos() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHEIRO_ALUNOS))) {
            for (Aluno a : alunos)
                pw.println(a.paraTexto());
            System.out.println("Alunos guardados em " + FICHEIRO_ALUNOS + " (" + alunos.size() + " registos).");
        } catch (IOException e) {
            System.out.println("Erro ao guardar alunos: " + e.getMessage());
        }
    }

    /**
     * Carrega alunos a partir de alunos.txt.
     * ATENÇÃO: deve ser chamado APÓS carregarLivros() para relinkar empréstimos.
     */
    public void carregarAlunos() {
        File f = new File(FICHEIRO_ALUNOS);
        if (!f.exists()) {
            System.out.println("Ficheiro " + FICHEIRO_ALUNOS + " nao encontrado. Iniciando com lista vazia.");
            return;
        }
        alunos.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.isBlank()) {
                    String[] partes = linha.split("\\|", -1);
                    if (partes.length < 4) continue;
                    Aluno a = new Aluno(partes[0], partes[1], partes[2]);
                    // Relinkar empréstimos pelo ISBN
                    if (!partes[3].isBlank()) {
                        for (String isbn : partes[3].split(",")) {
                            Livro l = buscarPorIsbn(isbn.trim());
                            if (l != null) {
                                // Marcar livro como não disponível e adicionar ao aluno
                                l.setDisponivel(false);
                                a.getEmprestimos(); // lista já está vazia
                                // Usamos reflexo manual: emprestar sem alterar disponivel (já foi carregado)
                                // Por isso chamamos direto para adicionar sem mudar flag
                                adicionarEmprestimoInterno(a, l);
                            }
                        }
                    }
                    alunos.add(a);
                }
            }
            System.out.println("Alunos carregados: " + alunos.size());
        } catch (IOException e) {
            System.out.println("Erro ao carregar alunos: " + e.getMessage());
        }
    }

    /**
     * Adiciona empréstimo internamente sem chamar emprestar() no livro
     * (evita mudar disponivel que já foi carregado do ficheiro).
     * Usa reflexão para aceder à lista privada.
     */
    private void adicionarEmprestimoInterno(Aluno aluno, Livro livro) {
        try {
            java.lang.reflect.Field f = Aluno.class.getDeclaredField("emprestimos");
            f.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Livro> lista = (List<Livro>) f.get(aluno);
            lista.add(livro);
        } catch (Exception e) {
            System.out.println("Aviso: nao foi possivel relinkar emprestimo para " + livro.getIsbn());
        }
    }

    /** Guarda livros e alunos */
    public void guardarTudo() {
        guardarLivros();
        guardarAlunos();
    }

    /** Carrega livros e depois alunos */
    public void carregarTudo() {
        carregarLivros();
        carregarAlunos();
    }

    // ------------------------------------------------------------------ //
    //  RELATÓRIOS
    // ------------------------------------------------------------------ //

    public void listarTodosLivros() {
        if (catalogo.isEmpty()) {
            System.out.println("Catalogo vazio.");
            return;
        }
        System.out.println("=== CATALOGO DE LIVROS ===");
        for (int i = 0; i < catalogo.size(); i++)
            System.out.printf("  %d. %s%n", i + 1, catalogo.get(i).info());
    }

    public void listarTodosAlunos() {
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno registado.");
            return;
        }
        System.out.println("=== ALUNOS REGISTADOS ===");
        for (int i = 0; i < alunos.size(); i++)
            System.out.printf("  %d. %s%n", i + 1, alunos.get(i));
    }

    public void listarLivrosDisponiveis() {
        System.out.println("=== LIVROS DISPONIVEIS ===");
        boolean algum = false;
        for (Livro l : catalogo)
            if (l.isDisponivel()) { System.out.println("  " + l.info()); algum = true; }
        if (!algum) System.out.println("  Nenhum livro disponivel.");
    }
}
