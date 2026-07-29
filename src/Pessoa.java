import java.io.Serializable;

public class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id; // Esta será a CHAVE usada na Árvore B+
    private String nome;
    private int idade;
    
    // TODO: Quando definirem o tema, adicionem os atributos específicos aqui.
    // Exemplo: private String diagnostico (se for hospital) 
    // Exemplo: private double salario (se for RH)

    public Pessoa(int id, String nome, int idade) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    
    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Idade: " + idade;
    }
}