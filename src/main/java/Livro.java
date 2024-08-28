public class Livro {
    private String nome;
    private String autor;
    private int ano;
    private String status;

    // Construtor
    public Livro(String nome, String autor, int ano) {
        this.nome = nome;
        this.autor = autor;
        this.ano = ano;
        this.status = "Disponível";
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getAutor() {
        return autor;
    }

    public int getAno() {
        return ano;
    }

    public String getStatus() {
        return status;
    }

    // Se necessário, você também pode adicionar setters
}