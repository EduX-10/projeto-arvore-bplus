import java.io.*;
import java.util.Scanner;

public class Main {
    private static final String NOME_ARQUIVO = "dados_arvore.dat";

    public static void main(String[] args) {
        ArvoreBMais arvore = carregarDados();
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        System.out.println("=== Sistema de Gerenciamento (Árvore B+) ===");

        while (opcao != 5) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Inserir nova Pessoa");
            System.out.println("2. Buscar Pessoa por ID");
            System.out.println("3. Listar todas as Pessoas");
            System.out.println("4. Remover Pessoa por ID");
            System.out.println("5. Sair e Salvar");
            System.out.print("Escolha uma opção: ");
            
            opcao = scanner.nextInt();

            if (opcao == 1) {
                System.out.print("Digite o ID: ");
                int id = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer do teclado
                System.out.print("Digite o Nome: ");
                String nome = scanner.nextLine();
                System.out.print("Digite a Idade: ");
                int idade = scanner.nextInt();
                
                Pessoa novaPessoa = new Pessoa(id, nome, idade);
                arvore.inserir(novaPessoa);
                System.out.println("Pessoa inserida com sucesso!");
            } 
            else if (opcao == 2) {
                System.out.print("Digite o ID para busca: ");
                int id = scanner.nextInt();
                Pessoa p = arvore.buscar(id);
                if (p != null) {
                    System.out.println("Encontrado: " + p);
                } else {
                    System.out.println("Pessoa não encontrada.");
                }
            }
            else if (opcao == 3) {
                System.out.println("\n--- Pessoas Cadastradas (Em Ordem) ---");
                arvore.listar();
            }
            else if (opcao == 4) {
                System.out.print("Digite o ID da Pessoa a ser removida: ");
                int id = scanner.nextInt();
                arvore.remover(id);
            }
            else if (opcao == 5) {
                System.out.println("Encerrando e salvando dados...");
            }
            else {
                System.out.println("Opção inválida! Tente novamente.");
            }
        }

        salvarDados(arvore);
        System.out.println("Dados salvos com sucesso em '" + NOME_ARQUIVO + "'. Programa encerrado.");
        scanner.close();
    }

    // --- Métodos de Persistência em Arquivo ---

    private static void salvarDados(ArvoreBMais arvore) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            oos.writeObject(arvore);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    private static ArvoreBMais carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (arquivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
                System.out.println("Dados anteriores carregados do arquivo com sucesso!");
                return (ArvoreBMais) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao carregar dados, criando nova árvore.");
            }
        } else {
            System.out.println("Nenhum arquivo anterior encontrado. Criando nova Árvore B+.");
        }
        return new ArvoreBMais(3); // Cria uma árvore de ordem 3 se não existir arquivo
    }
}