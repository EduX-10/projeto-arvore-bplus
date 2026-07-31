# 🌳 Sistema de Gerenciamento de Adotantes — Árvore B+ em Java

> **Projeto de Estrutura de Dados — 2026.1** > *Bacharelado em Ciência da Computação — Instituto Federal da Bahia (IFBA)*

---

## 📌 Sobre o Projeto

Este projeto consiste na implementação completa de uma **Árvore B+ (B+ Tree) de Ordem 3** desenvolvida em **Java**, com suporte à **persistência de dados em arquivo binário (`.dat`)**. 

O sistema foi desenhado para atuar como o **motor administrativo de alta performance (Back-end)** de uma plataforma de **Adoção de Animais**, gerenciando o cadastro e a triagem de **Adotantes e Tutores (Pessoas)** de forma eficiente, rápida e escalável.

---

## 🎯 Integração com o Ecossistema de Adoção

O projeto faz parte de uma solução em duas camadas:
1. **Front-end (Plataforma Web):** Interface em HTML/CSS para visualização dos pets e preenchimento de formulários de intenção de adoção.
2. **Back-end Administrativo (Este Repositório):** Sistema interno em Java utilizado pela equipe da ONG para armazenar, buscar, listar e remover cadastros de adotantes com complexidade logarítmica $O(\log n)$.

---

## ⚙️ Funcionalidades 

- [x] **➕ Inserção:** Adiciona novos adotantes mantendo a árvore balanceada. Suporta divisão de nós (*Split*) e promoção de chaves para níveis superiores.
- [x] **🔍 Busca O(log n):** Localiza instantaneamente um adotante pelo seu ID/CPF navegando pelos nós internos até as folhas.
- [x] **📜 Listagem Ordenada:** Percorre a lista encadeada no nível das folhas para imprimir todos os cadastros em ordem crescente de ID.
- [x] **🗑️ Remoção com Rebalanceamento:** Trata episódios de *underflow* realizando empréstimos entre nós vizinhos ou fusão (*merge*) de nós.
- [x] **💾 Persistência em Arquivo:** Salva automaticamente a estrutura inteira da árvore em `dados_arvore.dat` ao encerrar e restaura ao reiniciar o sistema.

---

## 🏗️ Estrutura de Arquivos

```text
arvore-b-mais-java/
│
├── src/
│   ├── Pessoa.java          # Entidade serializável (ID, Nome, Idade, etc.)
│   ├── ArvoreBMais.java     # Lógica da Árvore B+ (Inserção, Busca, Remoção, Split, Merge)
│   └── Main.java            # Interface via terminal (Menu interativo e Persistência)
│
├── .gitignore               # Ignora arquivos compilados e o banco .dat
├── dados_arvore.dat         # Banco de dados gerado automaticamente (não comitado)
└── README.md                # Documentação do repositório
