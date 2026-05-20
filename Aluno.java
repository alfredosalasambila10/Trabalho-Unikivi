import java.util.ArrayList;
import java.util.List;

public class Aluno {

    private String nome;
    private String numeroMatricula;
    private String curso;
    private List<Livro> emprestimos = new ArrayList<>();

    public Aluno(String nome, String numeroMatricula, String curso) {
        setNome(nome);
        setNumeroMatricula(numeroMatricula);
        setCurso(curso);
    }

    public String getNome() { return nome; }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome inválido");
        this.nome = nome;
    }

    public String getNumeroMatricula() { return numeroMatricula; }

    public void setNumeroMatricula(String numeroMatricula) {
        if (numeroMatricula == null || numeroMatricula.isBlank())
            throw new IllegalArgumentException("Matricula inválida");
        this.numeroMatricula = numeroMatricula;
    }

    public String getCurso() { return curso; }

    public void setCurso(String curso) {
        if (curso == null) curso = "";
        this.curso = curso;
    }

    public List<Livro> getEmprestimos() {
        return new ArrayList<>(emprestimos); // cópia para preservar encapsulamento
    }

    public boolean matricularLivro(Livro livro) {
        if (livro == null) throw new IllegalArgumentException("Livro nulo");
        if (!livro.emprestar()) return false; // já emprestado
        emprestimos.add(livro);
        return true;
    }

    public boolean devolverLivro(Livro livro) {
        if (livro == null) throw new IllegalArgumentException("Livro nulo");
        boolean removed = emprestimos.remove(livro);
        if (removed) livro.devolver();
        return removed;
    }

    public String mostrarEmprestimos() {
        if (emprestimos.isEmpty()) return nome + " nao tem emprestimos.";
        StringBuilder sb = new StringBuilder(nome + " tem os seguintes livros:\n");
        for (Livro l : emprestimos)
            sb.append("  - ").append(l.info()).append("\n");
        return sb.toString();
    }

    // Formato para guardar no ficheiro: nome|matricula|curso|isbn1,isbn2,...
    public String paraTexto() {
        StringBuilder isbnList = new StringBuilder();
        for (int i = 0; i < emprestimos.size(); i++) {
            if (i > 0) isbnList.append(",");
            isbnList.append(emprestimos.get(i).getIsbn());
        }
        return nome + "|" + numeroMatricula + "|" + curso + "|" + isbnList;
    }

    @Override
    public String toString() {
        return nome + " (" + numeroMatricula + ") - " + curso;
    }
}
