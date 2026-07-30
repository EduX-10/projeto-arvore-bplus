import java.io.Serializable;

public class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;                 // ID / Matrícula do Adotante (Chave da Árvore B+)
    private String nome;
    private int idade;
    private String telefone;        // Contato para a ONG falar com o adotante
    private String animalInteresse; // Ex: "Cão - Porte Médio", "Gato"

    public Pessoa(int id, String nome, int idade, String telefone, String animalInteresse) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.telefone = telefone;
        this.animalInteresse = animalInteresse;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    
    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Idade: " + idade + 
               " | Tel: " + telefone + " | Interesse: " + animalInteresse;
    }
}