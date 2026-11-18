# MinhaLoja

App Android (Kotlin + layouts XML) do desafio **Loja de Produtos com API, Firebase Auth e Firestore**.

## Integrantes

- Victor Hugo Soares Matos Brandão
- Thales de Melo Fontes

## O que o app faz

- **Login / cadastro** com e-mail e senha (Firebase Authentication), com mensagens de erro claras.
- **Lista de produtos** vinda da [Fake Store API](https://fakestoreapi.com/products) via Retrofit, com indicador de carregamento e tratamento de erro.
- **Favoritos** salvos no Cloud Firestore em `usuarios/{uid}/favoritos/{idDoProduto}` (id, título, preço, categoria e `salvoEm`).
- **Meus favoritos**: lista do Firestore (mais recentes primeiro, em tempo real) e remoção.
- **Sair** (logout) volta para o login. Usuário já logado abre direto na lista.
- Extras: foto do produto (Coil), atualização em tempo real (`addSnapshotListener`) e regras de segurança em `firestore.rules`.

## Como rodar

1. Abra a pasta do projeto no **Android Studio** (Ladybug ou mais recente) e aguarde o Gradle sincronizar.
2. No [Console do Firebase](https://console.firebase.google.com/), crie o projeto **MinhaLoja** e adicione um app Android com o pacote `com.victor.minhaloja` (ou altere `namespace` e `applicationId` em `app/build.gradle.kts`).
3. Baixe o `google-services.json` e coloque em `app/`. (Ele está no `.gitignore`, **não envie ao GitHub**.)
4. Em **Authentication → Método de login**, ative **E-mail/senha**.
5. Em **Firestore Database**, crie o banco em **modo de teste** (ou publique as regras de `firestore.rules`).
6. Execute o app em um emulador ou aparelho.

## Estrutura

```
app/src/main/java/com/victor/minhaloja/
├── model/    Produto.kt, Favorito.kt
├── network/  ProdutoService.kt, RetrofitClient.kt
├── data/     FavoritosRepository.kt
├── util/     Formatadores.kt
└── ui/       LoginActivity, ProdutosActivity, FavoritosActivity + adapters
```
