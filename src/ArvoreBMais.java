import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArvoreBMais implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int ordem;
    private No raiz;

    public ArvoreBMais(int ordem) {
        this.ordem = ordem;
        this.raiz = new NoFolha();
    }

    public void inserir(Pessoa pessoa) {
        No novaMetade = inserirRecursivo(raiz, pessoa);
        
        if (novaMetade != null) {
            NoInterno novaRaiz = new NoInterno();
            
            novaRaiz.chaves.add(novaMetade.chaveGuia); 
            
            novaRaiz.filhos.add(raiz);
            novaRaiz.filhos.add(novaMetade);
            
            raiz = novaRaiz; 
        }
    }

    private No inserirRecursivo(No noAtual, Pessoa pessoa) {
        int id = pessoa.getId();

        if (noAtual instanceof NoFolha) {
            NoFolha folha = (NoFolha) noAtual;
            
            int i = 0;
            while (i < folha.chaves.size() && folha.chaves.get(i) < id) {
                i++;
            }
            
            folha.chaves.add(i, id);
            folha.valores.add(i, pessoa);
            
            if (folha.chaves.size() == ordem) {
                return dividirFolha(folha); 
            }
        
            return null; 
        } 
        
        else {
            NoInterno interno = (NoInterno) noAtual;
            
            int i = 0;
            while (i < interno.chaves.size() && id >= interno.chaves.get(i)) {
                i++;
            }
            
            No novaMetadeDoFilho = inserirRecursivo(interno.filhos.get(i), pessoa);
            
            if (novaMetadeDoFilho != null) {
                int novoGuia = novaMetadeDoFilho.chaveGuia;
                
                int j = 0;
                while (j < interno.chaves.size() && interno.chaves.get(j) < novoGuia) {
                    j++;
                }
                
                interno.chaves.add(j, novoGuia);
                interno.filhos.add(j + 1, novaMetadeDoFilho);
                
                if (interno.chaves.size() == ordem) {
                    return dividirInterno(interno);
                }
            }
            
            return null;
        }
    }

    public Pessoa buscar(int id) {
        return buscarRecursivo(this.raiz, id);
    }

    private Pessoa buscarRecursivo(No noAtual, int id) {
        
        if (noAtual instanceof NoFolha) {
            NoFolha folha = (NoFolha) noAtual;
            for (Pessoa p : folha.valores) {
                if (p.getId() == id) {
                    return p;
                }
            }
            return null;
        } 
        
        else {
            NoInterno interno = (NoInterno) noAtual;
            int i = 0;
            
            while (i < interno.chaves.size() && id >= interno.chaves.get(i)) {
                i++;
            }
            
            return buscarRecursivo(interno.filhos.get(i), id);
        }
    }

    private No dividirFolha(NoFolha folhaAntiga) {
        NoFolha novaFolha = new NoFolha();
        
        int meio = folhaAntiga.chaves.size() / 2;
        
        while (folhaAntiga.chaves.size() > meio) {
            novaFolha.chaves.add(folhaAntiga.chaves.remove(meio));
            novaFolha.valores.add(folhaAntiga.valores.remove(meio));
        }
        
        novaFolha.proximo = folhaAntiga.proximo;
        folhaAntiga.proximo = novaFolha;
        
        novaFolha.chaveGuia = novaFolha.chaves.get(0);
        
        return novaFolha;
    }

    private No dividirInterno(NoInterno internoAntigo) {
        NoInterno novoInterno = new NoInterno();
        int meio = internoAntigo.chaves.size() / 2;
        
        int chavePromovida = internoAntigo.chaves.remove(meio);
        
        while (internoAntigo.chaves.size() > meio) {
            novoInterno.chaves.add(internoAntigo.chaves.remove(meio));
        }
        while (internoAntigo.filhos.size() > meio + 1) {
            novoInterno.filhos.add(internoAntigo.filhos.remove(meio + 1));
        }

        novoInterno.chaveGuia = chavePromovida;
        
        return novoInterno;
    }

    public void listar() {
        if (raiz == null) {
            System.out.println("A árvore está vazia!");
            return;
        }

        No noAtual = raiz;
        while (noAtual instanceof NoInterno) {
            noAtual = ((NoInterno) noAtual).filhos.get(0);
        }

        NoFolha folha = (NoFolha) noAtual;
        
        boolean encontrou = false;
        while (folha != null) {
            for (Pessoa p : folha.valores) {
                System.out.println(p);
                encontrou = true;
            }
            folha = folha.proximo; 
        }

        if (!encontrou) {
            System.out.println("Nenhuma pessoa cadastrada.");
        }
    }

    
    public void remover(int id) {
        if (raiz == null) {
            System.out.println("A árvore está vazia!");
            return;
        }

        removerRecursivo(raiz, id, null, -1);

        if (raiz instanceof NoInterno && raiz.chaves.isEmpty()) {
            raiz = ((NoInterno) raiz).filhos.get(0);
        }
    }

    private void removerRecursivo(No noAtual, int id, NoInterno pai, int indiceNoPai) {
        
        if (noAtual instanceof NoInterno) {
            NoInterno interno = (NoInterno) noAtual;
            int i = 0;
            while (i < interno.chaves.size() && id >= interno.chaves.get(i)) {
                i++;
            }
            removerRecursivo(interno.filhos.get(i), id, interno, i);
            
            if (interno.filhos.get(i).chaves.size() < (ordem - 1) / 2) {
                lidarComUnderflow(interno, i);
            }
        } 
        
        else {
            NoFolha folha = (NoFolha) noAtual;
            int i = 0;
            while (i < folha.chaves.size() && folha.chaves.get(i) < id) {
                i++;
            }
            
            if (i < folha.chaves.size() && folha.chaves.get(i) == id) {
                folha.chaves.remove(i);
                folha.valores.remove(i);
                System.out.println("Pessoa com ID " + id + " removida com sucesso!");
            } else {
                System.out.println("Pessoa com ID " + id + " não encontrada.");
            }
        }
    }

    private void lidarComUnderflow(NoInterno pai, int indiceFilhoComProblema) {
        No filhoProblematico = pai.filhos.get(indiceFilhoComProblema);
        int capacidadeMinima = (ordem - 1) / 2;

        No irmaoEsquerdo = (indiceFilhoComProblema > 0) ? pai.filhos.get(indiceFilhoComProblema - 1) : null;
        No irmaoDireito = (indiceFilhoComProblema < pai.filhos.size() - 1) ? pai.filhos.get(indiceFilhoComProblema + 1) : null;

        if (irmaoEsquerdo != null && irmaoEsquerdo.chaves.size() > capacidadeMinima) {
            pegarEmprestadoDaEsquerda(pai, indiceFilhoComProblema, filhoProblematico, irmaoEsquerdo);
            return;
        }

        if (irmaoDireito != null && irmaoDireito.chaves.size() > capacidadeMinima) {
            pegarEmprestadoDaDireita(pai, indiceFilhoComProblema, filhoProblematico, irmaoDireito);
            return;
        }

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
            
            folha.chaves.add(0, folhaEsq.chaves.remove(folhaEsq.chaves.size() - 1));
            folha.valores.add(0, folhaEsq.valores.remove(folhaEsq.valores.size() - 1));
        
            pai.chaves.set(indiceFilho - 1, folha.chaves.get(0));
        } else {
            NoInterno interno = (NoInterno) filho;
            NoInterno internoEsq = (NoInterno) esquerdo;

            interno.chaves.add(0, pai.chaves.get(indiceFilho - 1));

            interno.filhos.add(0, internoEsq.filhos.remove(internoEsq.filhos.size() - 1));

            pai.chaves.set(indiceFilho - 1, internoEsq.chaves.remove(internoEsq.chaves.size() - 1));
        }
    }

    private void pegarEmprestadoDaDireita(NoInterno pai, int indiceFilho, No filho, No direito) {
        if (filho instanceof NoFolha) {
            NoFolha folha = (NoFolha) filho;
            NoFolha folhaDir = (NoFolha) direito;
            
            folha.chaves.add(folhaDir.chaves.remove(0));
            folha.valores.add(folhaDir.valores.remove(0));
            
            pai.chaves.set(indiceFilho, folhaDir.chaves.get(0));
        } else {
            NoInterno interno = (NoInterno) filho;
            NoInterno internoDir = (NoInterno) direito;

            interno.chaves.add(pai.chaves.get(indiceFilho));

            interno.filhos.add(internoDir.filhos.remove(0));

                pai.chaves.set(indiceFilho, internoDir.chaves.remove(0));
        }
    }

    private void fundirNos(NoInterno pai, int indiceEsquerdo, No esquerdo, No direito) {
        if (esquerdo instanceof NoFolha) {
            NoFolha folhaEsq = (NoFolha) esquerdo;
            NoFolha folhaDir = (NoFolha) direito;
        
            folhaEsq.chaves.addAll(folhaDir.chaves);
            folhaEsq.valores.addAll(folhaDir.valores);
            
            folhaEsq.proximo = folhaDir.proximo;
            
            pai.chaves.remove(indiceEsquerdo);
            pai.filhos.remove(indiceEsquerdo + 1);
        } else {
            NoInterno internoEsq = (NoInterno) esquerdo;
            NoInterno internoDir = (NoInterno) direito;

            internoEsq.chaves.add(pai.chaves.get(indiceEsquerdo));

            internoEsq.chaves.addAll(internoDir.chaves);
            internoEsq.filhos.addAll(internoDir.filhos);

            pai.chaves.remove(indiceEsquerdo);
            pai.filhos.remove(indiceEsquerdo + 1);
        }
    }

    
    static abstract class No implements Serializable {
        List<Integer> chaves = new ArrayList<>();
        Integer chaveGuia;
    }

    static class NoInterno extends No {
        List<No> filhos = new ArrayList<>();
    }

    static class NoFolha extends No {
        List<Pessoa> valores = new ArrayList<>();
        NoFolha proximo;
    }
}