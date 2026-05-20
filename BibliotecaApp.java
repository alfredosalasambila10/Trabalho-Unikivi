import java.util.List;
import java.util.Scanner;

/**
 * BibliotecaApp — Sistema de Biblioteca com entrada manual pelo utilizador.
 * Os dados são persistidos automaticamente em livros.txt e alunos.txt.
 */
public class BibliotecaApp {

    private static final Scanner sc = new Scanner(System.in);
    private static final Biblioteca biblioteca = new Biblioteca();

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   SISTEMA DE BIBLIOTECA — UNIKIVI / POO Java    ");
        System.out.println("=================================================");

        // Carregar dados guardados anteriormente
        biblioteca.carregarTudo();

        boolean sair = false;
        while (!sair) {
            exibirMenu();
            int opcao = lerInteiro("Escolha uma opcao: ");
            System.out.println();
            switch (opcao) {
                case 1  -> adicionarLivro();
                case 2  -> adicionarAluno();
                case 3  -> emprestarLivro();
                case 4  -> devolverLivro();
                case 5  -> biblioteca.listarTodosLivros();
                case 6  -> biblioteca.listarLivrosDisponiveis();
                case 7  -> biblioteca.listarTodosAlunos();
                case 8  -> verEmprestimosAluno();
                case 9  -> buscarLivro();
                case 10 -> {
                    biblioteca.guardarTudo();
                    System.out.println("Dados guardados com sucesso!");
                }
                case 0  -> {
                    biblioteca.guardarTudo();
                    System.out.println("Dados guardados. Ate logo!");
                    sair = true;
                }
                default -> System.out.println("Opcao invalida. Tente novamente.");
            }
            System.out.println();
        }
        sc.close();
    }

    // ------------------------------------------------------------------ //
    //  MENU
    // ------------------------------------------------------------------ //

    private static void exibirMenu() {
        System.out.println("----------- MENU PRINCIPAL -----------");
        System.out.println("  1. Adicionar livro a biblioteca");
        System.out.println("  2. Registar aluno");
        System.out.println("  3. Emprestar livro a aluno");
        System.out.println("  4. Devolver livro");
        System.out.println("  5. Livros da biblioteca");
        System.out.println("  6. Livros disponiveis");
        System.out.println("  7. Alunos Listados ");
        System.out.println("  8. Ver emprestimos de um aluno");
        System.out.println("  9. Buscar livro (titulo/autor/isbn)");
        System.out.println(" 10. Guardar dados manualmente");
        System.out.println("  0. Sair (guarda automaticamente)");
        System.out.println("--------------------------------------");
    }

    // ------------------------------------------------------------------ //
    //  OPERAÇÕES
    // ------------------------------------------------------------------ //

    private static void adicionarLivro() {
        System.out.println("--- Adicionar Livro ---");
        String titulo = lerString("Titulo: ");
        String autor  = lerString("Autor: ");
        String isbn   = lerIsbn();
        try {
            Livro l = new Livro(titulo, autor, isbn);
            biblioteca.adicionarLivro(l);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao adicionar livro: " + e.getMessage());
        }
    }

    private static void adicionarAluno() {
        System.out.println("--- Registar Aluno ---");
        String nome       = lerString("Nome: ");
        String matricula  = lerString("Numero de matricula: ");
        String curso      = lerString("Curso: ");
        try {
            Aluno a = new Aluno(nome, matricula, curso);
            biblioteca.adicionarAluno(a);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao registar aluno: " + e.getMessage());
        }
    }

    private static void emprestarLivro() {
        System.out.println("--- Emprestar Livro ---");
        String matricula = lerString("Matricula do aluno: ");
        String isbn      = lerString("ISBN do livro (13 digitos): ");
        biblioteca.emprestarLivro(matricula, isbn);
    }

    private static void devolverLivro() {
        System.out.println("--- Devolver Livro ---");
        String matricula = lerString("Matricula do aluno: ");
        String isbn      = lerString("ISBN do livro (13 digitos): ");
        biblioteca.devolverLivro(matricula, isbn);
    }

    private static void verEmprestimosAluno() {
        String matricula = lerString("Matricula do aluno: ");
        Aluno a = biblioteca.buscarAlunoPorMatricula(matricula);
        if (a == null)
            System.out.println("Aluno nao encontrado.");
        else
            System.out.println(a.mostrarEmprestimos());
    }

    private static void buscarLivro() {
        System.out.println("--- Buscar Livro ---");
        System.out.println("  1. Por titulo");
        System.out.println("  2. Por autor");
        System.out.println("  3. Por ISBN");
        int op = lerInteiro("Escolha: ");
        switch (op) {
            case 1 -> {
                String t = lerString("Titulo (ou parte): ");
                List<Livro> res = biblioteca.buscarPorTitulo(t);
                imprimirResultados(res);
            }
            case 2 -> {
                String a = lerString("Autor (ou parte): ");
                List<Livro> res = biblioteca.buscarPorAutor(a);
                imprimirResultados(res);
            }
            case 3 -> {
                String i = lerString("ISBN (13 digitos): ");
                Livro l = biblioteca.buscarPorIsbn(i);
                if (l == null) System.out.println("Livro nao encontrado.");
                else System.out.println("Encontrado: " + l.info());
            }
            default -> System.out.println("Opcao invalida.");
        }
    }

    private static void imprimirResultados(List<Livro> lista) {
        if (lista.isEmpty()) { System.out.println("Nenhum livro encontrado."); return; }
        System.out.println("Resultados (" + lista.size() + "):");
        for (Livro l : lista) System.out.println("  " + l.info());
    }

    // ------------------------------------------------------------------ //
    //  UTILITÁRIOS DE LEITURA
    // ------------------------------------------------------------------ //

    private static String lerString(String mensagem) {
        System.out.print(mensagem);
        return sc.nextLine().trim();
    }

    private static int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        try {
            int val = Integer.parseInt(sc.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Lê e valida um ISBN de 13 caracteres, pedindo novamente se inválido */
    private static String lerIsbn() {
        while (true) {
            System.out.print("ISBN (exatamente 13 caracteres): ");
            String isbn = sc.nextLine().trim();
            if (isbn.length() == 13) return isbn;
            System.out.println("  ISBN invalido! Deve ter exactamente 13 caracteres. Tente novamente.");
        }
    }
}
