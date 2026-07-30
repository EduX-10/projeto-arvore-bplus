import java.io.*;
import java.util.InputMismatchException;
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
            System.out.println("1. Inserir novo Adotante");
            System.out.println("2. Buscar Adotante por ID");
            System.out.println("3. Listar todos os Adotantes");
            System.out.println("4. Remover Adotante por ID");
            System.out.println("5. Sair e Salvar");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();

                if (opcao == 1) {
                    System.out.print("Digite o ID (CPF/Matrícula): ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Limpar buffer

                    // Trava de Segurança: Verificação de ID Duplicado
                    if (arvore.buscar(id) != null) {
                        System.out.println("Erro: Já existe um adotante cadastrado com o ID " + id + ".");
                        continue;
                    }

                    System.out.print("Digite o Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Digite a Idade: ");
                    int idade = scanner.nextInt();
                    scanner.nextLine(); // Limpar buffer

                    System.out.print("Digite o Telefone: ");
                    String telefone = scanner.nextLine();
                    System.out.print("Digite o Animal de Interesse (Ex: Cão, Gato): ");
                    String animalInteresse = scanner.nextLine();
                    
                    Pessoa novaPessoa = new Pessoa(id, nome, idade, telefone, animalInteresse);
                    arvore.inserir(novaPessoa);
                    System.out.println("Adotante inserido com sucesso!");
                } 
                else if (opcao == 2) {
                    System.out.print("Digite o ID para busca: ");
                    int id = scanner.nextInt();
                    Pessoa p = arvore.buscar(id);
                    if (p != null) {
                        System.out.println("Encontrado: " + p);
                    } else {
                        System.out.println("Adotante não encontrado.");
                    }
                }
                else if (opcao == 3) {
                    System.out.println("\n--- Adotantes Cadastrados (Em Ordem) ---");
                    arvore.listar();
                }
                else if (opcao == 4) {
                    System.out.print("Digite o ID do Adotante a ser removido: ");
                    int id = scanner.nextInt();
                    arvore.remover(id);
                }
                else if (opcao == 5) {
                    System.out.println("Encerrando e salvando dados...");
                }
                else {
                    System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                // Trava de Segurança: Evita que o programa quebre se o usuário digitar letras
                System.out.println("Entrada inválida! Por favor, digite apenas números onde solicitado.");
                scanner.nextLine(); // Limpa o buffer corrompido para evitar loop infinito
            }
        }

        salvarDados(arvore);
        System.out.println("Dados salvos com sucesso em '" + NOME_ARQUIVO + "'. Programa encerrado.");
        scanner.close();
    }

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
        return new ArvoreBMais(3);
    }
}