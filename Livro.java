public class Livro {

    private String titulo;
    private String autor;
    private String isbn; // ISBN com 13 caracteres
    private boolean disponivel = true;

    public Livro(String titulo, String autor, String isbn) {
        setTitulo(titulo);
        setAutor(autor);
        setIsbn(isbn);
    }

    public String getTitulo() { return titulo; }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank())
            throw new IllegalArgumentException("Título inválido");
        this.titulo = titulo;
    }

    public String getAutor() { return autor; }

    public void setAutor(String autor) {
        if (autor == null || autor.isBlank())
            throw new IllegalArgumentException("Autor inválido");
        this.autor = autor;
    }

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.length() != 6)
            throw new IllegalArgumentException("ISBN deve ter 6 caracteres");
        this.isbn = isbn;
    }

    public boolean isDisponivel() { return disponivel; }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public boolean emprestar() {
        if (!disponivel) return false;
        disponivel = false;
        return true;
    }

    public void devolver() {
        disponivel = true;
    }

    public String info() {
        return String.format("%s - %s (ISBN: %s) Disponivel: %s",
                titulo, autor, isbn, disponivel ? "Sim" : "Nao");
    }

    // Formato para guardar no ficheiro de texto: titulo|autor|isbn|disponivel
    public String paraTexto() {
        return titulo + "|" + autor + "|" + isbn + "|" + disponivel;
    }

    // Cria Livro a partir de uma linha do ficheiro de texto
    public static Livro deTexto(String linha) {
        String[] partes = linha.split("\\|");
        if (partes.length != 4)
            throw new IllegalArgumentException("Linha invalida: " + linha);
        Livro l = new Livro(partes[0], partes[1], partes[2]);
        l.setDisponivel(Boolean.parseBoolean(partes[3]));
        return l;
    }

    @Override
    public String toString() { return info(); }
}
