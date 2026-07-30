import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArvoreBMais implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int ordem;
    private No raiz;

    public ArvoreBMais(int ordem) {
        this.ordem = ordem;
        this.raiz = new NoFolha(); // Começa com uma folha vazia
    }

    // Método principal de inserção
    // (Chamado pelo Menu)
    public void inserir(Pessoa pessoa) {
        // Chamamos o método auxiliar. Se ele retornar um "novo nó", 
        // significa que a raiz atual estourou o limite e rachou ao meio!
        No novaMetade = inserirRecursivo(raiz, pessoa);
        
        if (novaMetade != null) {
            System.out.println("A raiz rachou! A árvore vai crescer um nível para cima.");
            
            // Criamos uma nova placa de trânsito para ser a nova raiz
            NoInterno novaRaiz = new NoInterno();
            
            // A chave-guia que sobe é a que foi promovida pelo split (não a
            // primeira chave "crua" do nó novo - ver dividirInterno/dividirFolha)
            novaRaiz.chaves.add(novaMetade.chaveGuia); 
            
            // Conectamos a raiz antiga na esquerda (filho 1), e a nova metade na direita (filho 2)
            novaRaiz.filhos.add(raiz);
            novaRaiz.filhos.add(novaMetade);
            
            // Coroamos a nova raiz oficial da árvore
            raiz = novaRaiz;
        }
    }

    // Método auxiliar recursivo
    // Método auxiliar recursivo completo
    private No inserirRecursivo(No noAtual, Pessoa pessoa) {
        int id = pessoa.getId();

        // --- CENÁRIO 1: Chegamos no vagão de dados (Nó Folha) ---
        if (noAtual instanceof NoFolha) {
            NoFolha folha = (NoFolha) noAtual;
            
            // 1. Descobrimos a posição certa para manter tudo em ordem crescente
            int i = 0;
            while (i < folha.chaves.size() && folha.chaves.get(i) < id) {
                i++;
            }
            
            // 2. Inserimos a chave (ID) e a Pessoa nas suas respectivas listas
            folha.chaves.add(i, id);
            folha.valores.add(i, pessoa);
            
            // 3. Verificamos se a folha ficou superlotada (atingiu a 'ordem')
            if (folha.chaves.size() == ordem) {
                // Ops, quebrou! Chamamos a função de dividir e devolvemos a nova metade para o nó de cima.
                return dividirFolha(folha); 
            }
            
            // Tudo coube perfeitamente, não quebrou nada.
            return null; 
        } 
        
        // --- CENÁRIO 2: Estamos em uma placa de trânsito (Nó Interno) ---
        else {
            NoInterno interno = (NoInterno) noAtual;
            
            // 1. Descobrimos por qual porta (filho) devemos descer
            int i = 0;
            while (i < interno.chaves.size() && id >= interno.chaves.get(i)) {
                i++;
            }
            
            // 2. Descemos recursivamente. Se o filho rachar, ele nos devolve a nova metade!
            No novaMetadeDoFilho = inserirRecursivo(interno.filhos.get(i), pessoa);
            
            // 3. Se o filho rachou, precisamos acomodar o novo guia e a nova metade aqui neste nó interno
            if (novaMetadeDoFilho != null) {
                
                // O guia que sobe para este nó é a chave promovida pelo split do filho
                // (não necessariamente a primeira chave do nó novo - ver dividirInterno)
                int novoGuia = novaMetadeDoFilho.chaveGuia;
                
                // Encontramos a posição para colocar o guia mantendo a ordem crescente
                int j = 0;
                while (j < interno.chaves.size() && interno.chaves.get(j) < novoGuia) {
                    j++;
                }
                
                // Inserimos o guia e a porta (filho) correspondente
                interno.chaves.add(j, novoGuia);
                interno.filhos.add(j + 1, novaMetadeDoFilho);
                
                // Efeito Dominó: Se esta placa de trânsito também lotar, ela racha!
                if (interno.chaves.size() == ordem) {
                    return dividirInterno(interno);
                }
            }
            
            return null; // O filho não rachou, então este nó também fica tranquilo.
        }
    }

    // Método de busca
    // Método de busca principal (chamado pelo Menu)
    public Pessoa buscar(int id) {
        return buscarRecursivo(this.raiz, id);
    }

    // Método auxiliar que faz o trabalho pesado descendo a árvore
    private Pessoa buscarRecursivo(No noAtual, int id) {
        
        // CENÁRIO 1: Chegamos no "vagão de dados" (Nó Folha)
        if (noAtual instanceof NoFolha) {
            NoFolha folha = (NoFolha) noAtual;
            // Procuramos a pessoa na lista de valores desta folha
            for (Pessoa p : folha.valores) {
                if (p.getId() == id) {
                    return p; // Achamos!
                }
            }
            return null; // A pessoa não existe na árvore
        } 
        
        // CENÁRIO 2: Estamos em uma "placa de trânsito" (Nó Interno)
        else {
            NoInterno interno = (NoInterno) noAtual;
            int i = 0;
            
            // Comparamos o ID que queremos com as chaves (guias) do nó
            // Enquanto o ID for MAIOR ou IGUAL à chave atual, vamos para a direita
            while (i < interno.chaves.size() && id >= interno.chaves.get(i)) {
                i++;
            }
            
            // Achamos o caminho! Descemos para o filho correspondente
            return buscarRecursivo(interno.filhos.get(i), id);
        }
    }

    // Método para rachar uma folha ao meio
    private No dividirFolha(NoFolha folhaAntiga) {
        NoFolha novaFolha = new NoFolha();
        
        // Encontramos o ponto de corte (o meio da lista)
        int meio = folhaAntiga.chaves.size() / 2;
        
        // Movemos os dados da folha antiga para a nova folha
        while (folhaAntiga.chaves.size() > meio) {
            // Removemos da antiga e adicionamos na nova (mantendo a ordem)
            novaFolha.chaves.add(folhaAntiga.chaves.remove(meio));
            novaFolha.valores.add(folhaAntiga.valores.remove(meio));
        }
        
        // A Mágica da Árvore B+: Reconectar os vagões (Lista Encadeada)
        // A nova folha aponta para onde a antiga apontava
        novaFolha.proximo = folhaAntiga.proximo;
        // A antiga agora aponta para a nova folha
        folhaAntiga.proximo = novaFolha;
        
        // Em folhas, a chave-guia é simplesmente a primeira chave que ficou na
        // nova folha - ela permanece duplicada na folha (característica da B+)
        novaFolha.chaveGuia = novaFolha.chaves.get(0);
        
        // Devolvemos a nova folha para o nó de cima criar a "placa de trânsito"
        return novaFolha;
    }

    // Método para rachar um nó interno ao meio
    private No dividirInterno(NoInterno internoAntigo) {
        NoInterno novoInterno = new NoInterno();
        int meio = internoAntigo.chaves.size() / 2;
        
        // IMPORTANTE: diferente da folha, um nó interno tem sempre
        // (chaves + 1) filhos. Por isso a chave do meio não pode ficar
        // duplicada nos dois lados - ela precisa "subir" de vez para o pai,
        // sem sobrar em nenhum dos dois nós resultantes.
        int chavePromovida = internoAntigo.chaves.remove(meio);
        
        // O que sobrou depois do meio (chaves e filhos) vai para o novo nó
        while (internoAntigo.chaves.size() > meio) {
            novoInterno.chaves.add(internoAntigo.chaves.remove(meio));
        }
        while (internoAntigo.filhos.size() > meio + 1) {
            novoInterno.filhos.add(internoAntigo.filhos.remove(meio + 1));
        }
        
        // Guardamos a chave promovida para o nó de cima usar como guia
        novoInterno.chaveGuia = chavePromovida;
        
        return novoInterno;
    }

    // Método para imprimir todas as pessoas em ordem crescente de ID
    public void listar() {
        if (raiz == null) {
            System.out.println("A árvore está vazia!");
            return;
        }

        No noAtual = raiz;
        // 1. Desce até a primeira folha (a mais à esquerda de todas)
        while (noAtual instanceof NoInterno) {
            noAtual = ((NoInterno) noAtual).filhos.get(0);
        }

        NoFolha folha = (NoFolha) noAtual;
        
        // 2. Percorre a lista encadeada das folhas do início ao fim
        boolean encontrou = false;
        while (folha != null) {
            for (Pessoa p : folha.valores) {
                System.out.println(p);
                encontrou = true;
            }
            folha = folha.proximo; // Pula para o próximo vagão
        }

        if (!encontrou) {
            System.out.println("Nenhuma pessoa cadastrada.");
        }
    }

    // Método principal chamado pelo Menu
    public void remover(int id) {
        if (raiz == null) {
            System.out.println("A árvore está vazia!");
            return;
        }

        // Chamamos a remoção. O booleano nos diz se a altura da árvore diminuiu
        removerRecursivo(raiz, id, null, -1);

        // CENÁRIO 4 (Extremo): A raiz antiga perdeu todos os guias. 
        // O único filho dela passa a ser a nova raiz (a árvore encolhe).
        if (raiz instanceof NoInterno && raiz.chaves.isEmpty()) {
            raiz = ((NoInterno) raiz).filhos.get(0);
        }
    }

    // Método recursivo que desce até a folha
    private void removerRecursivo(No noAtual, int id, NoInterno pai, int indiceNoPai) {
        
        // --- SE ESTAMOS EM UM NÓ INTERNO (Placa de Trânsito) ---
        if (noAtual instanceof NoInterno) {
            NoInterno interno = (NoInterno) noAtual;
            int i = 0;
            while (i < interno.chaves.size() && id >= interno.chaves.get(i)) {
                i++;
            }
            // Descemos para o filho correto
            removerRecursivo(interno.filhos.get(i), id, interno, i);
            
            // Após voltar da recursão, verificamos se o filho sofreu underflow
            if (interno.filhos.get(i).chaves.size() < (ordem - 1) / 2) {
                lidarComUnderflow(interno, i);
            }
        } 
        
        // --- SE ESTAMOS EM UMA FOLHA (Vagão de Dados) ---
        else {
            NoFolha folha = (NoFolha) noAtual;
            int i = 0;
            while (i < folha.chaves.size() && folha.chaves.get(i) < id) {
                i++;
            }
            
            // Verifica se achou a pessoa
            if (i < folha.chaves.size() && folha.chaves.get(i) == id) {
                // CENÁRIO 1: Simplesmente removemos o ID e o Valor da folha
                folha.chaves.remove(i);
                folha.valores.remove(i);
                System.out.println("Pessoa com ID " + id + " removida com sucesso!");
            } else {
                System.out.println("Pessoa com ID " + id + " não encontrada.");
            }
            // O underflow da folha será tratado no nó pai quando a recursão voltar!
        }
    }

    // Método que resolve a falta de chaves em um filho
    private void lidarComUnderflow(NoInterno pai, int indiceFilhoComProblema) {
        No filhoProblematico = pai.filhos.get(indiceFilhoComProblema);
        int capacidadeMinima = (ordem - 1) / 2;

        No irmaoEsquerdo = (indiceFilhoComProblema > 0) ? pai.filhos.get(indiceFilhoComProblema - 1) : null;
        No irmaoDireito = (indiceFilhoComProblema < pai.filhos.size() - 1) ? pai.filhos.get(indiceFilhoComProblema + 1) : null;

        // CENÁRIO 2.A: Tentar pegar emprestado do irmão da ESQUERDA
        if (irmaoEsquerdo != null && irmaoEsquerdo.chaves.size() > capacidadeMinima) {
            pegarEmprestadoDaEsquerda(pai, indiceFilhoComProblema, filhoProblematico, irmaoEsquerdo);
            return;
        }

        // CENÁRIO 2.B: Tentar pegar emprestado do irmão da DIREITA
        if (irmaoDireito != null && irmaoDireito.chaves.size() > capacidadeMinima) {
            pegarEmprestadoDaDireita(pai, indiceFilhoComProblema, filhoProblematico, irmaoDireito);
            return;
        }

        // CENÁRIO 3: Ninguém pode emprestar. Precisamos FUNDIR (Merge) os vagões.
        // Preferimos sempre fundir com o irmão da esquerda, se ele existir.
        if (irmaoEsquerdo != null) {
            fundirNos(pai, indiceFilhoComProblema - 1, irmaoEsquerdo, filhoProblematico);
        } else {
            fundirNos(pai, indiceFilhoComProblema, filhoProblematico, irmaoDireito);
        }
    }

    private void pegarEmprestadoDaEsquerda(NoInterno pai, int indiceFilho, No filho, No esquerdo) {
        if (filho instanceof NoFolha) {
            NoFolha folha = (NoFolha) filho;
            NoFolha folhaEsq = (NoFolha) esquerdo;
            
            // Pega o ÚLTIMO elemento do irmão esquerdo e coloca no INÍCIO do filho
            folha.chaves.add(0, folhaEsq.chaves.remove(folhaEsq.chaves.size() - 1));
            folha.valores.add(0, folhaEsq.valores.remove(folhaEsq.valores.size() - 1));
            
            // Atualiza a placa de trânsito (nó pai) com a nova primeira chave do filho
            pai.chaves.set(indiceFilho - 1, folha.chaves.get(0));
        } else {
            // Mesma ideia, mas para nós internos: a chave não vem do irmão
            // direto para o filho - ela passa "pelo pai" (rotação clássica de
            // árvore B), porque nós internos guardam apenas chaves-guia, não
            // os dados em si.
            NoInterno interno = (NoInterno) filho;
            NoInterno internoEsq = (NoInterno) esquerdo;

            // A chave que estava separando os dois nós no pai desce para
            // virar a primeira chave do filho
            interno.chaves.add(0, pai.chaves.get(indiceFilho - 1));

            // O último filho do irmão esquerdo passa a ser o primeiro filho deste nó
            interno.filhos.add(0, internoEsq.filhos.remove(internoEsq.filhos.size() - 1));

            // A última chave do irmão esquerdo sobe para ocupar o lugar no pai
            pai.chaves.set(indiceFilho - 1, internoEsq.chaves.remove(internoEsq.chaves.size() - 1));
        }
    }

    private void pegarEmprestadoDaDireita(NoInterno pai, int indiceFilho, No filho, No direito) {
        if (filho instanceof NoFolha) {
            NoFolha folha = (NoFolha) filho;
            NoFolha folhaDir = (NoFolha) direito;
            
            // Pega o PRIMEIRO elemento do irmão direito e coloca no FINAL do filho
            folha.chaves.add(folhaDir.chaves.remove(0));
            folha.valores.add(folhaDir.valores.remove(0));
            
            // Atualiza a placa de trânsito (nó pai) com a nova primeira chave do irmão direito
            pai.chaves.set(indiceFilho, folhaDir.chaves.get(0));
        } else {
            NoInterno interno = (NoInterno) filho;
            NoInterno internoDir = (NoInterno) direito;

            // A chave que separava os dois nós no pai desce para virar a
            // última chave deste nó
            interno.chaves.add(pai.chaves.get(indiceFilho));

            // O primeiro filho do irmão direito passa a ser o último filho deste nó
            interno.filhos.add(internoDir.filhos.remove(0));

            // A primeira chave do irmão direito sobe para ocupar o lugar no pai
            pai.chaves.set(indiceFilho, internoDir.chaves.remove(0));
        }
    }

    private void fundirNos(NoInterno pai, int indiceEsquerdo, No esquerdo, No direito) {
        if (esquerdo instanceof NoFolha) {
            NoFolha folhaEsq = (NoFolha) esquerdo;
            NoFolha folhaDir = (NoFolha) direito;
            
            // Movemos todos os dados do nó direito para dentro do nó esquerdo
            folhaEsq.chaves.addAll(folhaDir.chaves);
            folhaEsq.valores.addAll(folhaDir.valores);
            
            // Reconectamos o trem (lista encadeada): o esquerdo pula o direito que foi esvaziado
            folhaEsq.proximo = folhaDir.proximo;
            
            // Deletamos a placa de trânsito que separava os dois no pai
            pai.chaves.remove(indiceEsquerdo);
            pai.filhos.remove(indiceEsquerdo + 1); // Removemos o ponteiro para o nó direito
        } else {
            NoInterno internoEsq = (NoInterno) esquerdo;
            NoInterno internoDir = (NoInterno) direito;

            // A chave que separava os dois nós no pai "desce" e passa a viver
            // dentro do nó fundido, entre as chaves que vieram de cada lado
            internoEsq.chaves.add(pai.chaves.get(indiceEsquerdo));

            // Juntamos as chaves e os filhos do nó direito dentro do esquerdo
            internoEsq.chaves.addAll(internoDir.chaves);
            internoEsq.filhos.addAll(internoDir.filhos);

            // Deletamos a placa de trânsito que separava os dois no pai
            pai.chaves.remove(indiceEsquerdo);
            pai.filhos.remove(indiceEsquerdo + 1);
        }
    }

    // --- Classes Internas para os Nós ---
    
    // Classe base abstrata
    static abstract class No implements Serializable {
        List<Integer> chaves = new ArrayList<>();
        // Chave a ser promovida para o pai quando este nó nasce de um split.
        // Para folhas: é a primeira chave (fica duplicada, como manda a B+).
        // Para nós internos: é a chave do meio que foi removida no split
        // (não fica duplicada em nenhum lado, senão a conta de filhos não fecha).
        Integer chaveGuia;
    }

    // Nó Interno (As "Placas de trânsito")
    static class NoInterno extends No {
        List<No> filhos = new ArrayList<>();
    }

    // Nó Folha (Os "Vagões de dados")
    static class NoFolha extends No {
        List<Pessoa> valores = new ArrayList<>();
        NoFolha proximo; // Ponteiro para a próxima folha (lista encadeada)
    }
}
