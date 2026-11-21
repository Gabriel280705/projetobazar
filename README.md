# 🛍️ API do Bazar

API REST para controle de **estoque e vendas de um bazar**, desenvolvida como projeto final da disciplina de Arquitetura Orientada a Serviços (Fatec Jales).

## Sobre o projeto

### Problema
Em um bazar é comum vender uma peça que já acabou ou perder o controle do que ainda resta no estoque. Muitas vezes isso é feito em papel ou planilha, o que gera erros.

### Solução
Uma API que organiza os produtos por categoria, controla o estoque e registra as vendas. Ao vender, o estoque é atualizado automaticamente e o sistema impede a venda de itens sem estoque. Como é uma API REST, qualquer aplicação (site, app, planilha) pode consumir esses serviços.

## Funcionalidades

- Cadastro, consulta, atualização e exclusão de **categorias**
- Cadastro, consulta, atualização e exclusão de **produtos** (cada produto pertence a uma categoria)
- Busca de produtos por parte do nome e por categoria
- Registro, consulta e cancelamento de **vendas**
- Validação dos dados enviados
- Documentação interativa com Swagger

### Regras de negócio

- Não é possível vender mais do que o estoque disponível
- A venda reduz o estoque e calcula o valor total (preço × quantidade) automaticamente
- Cancelar uma venda devolve os itens ao estoque
- Não é possível excluir uma categoria que tenha produtos, nem um produto que tenha vendas
- Não é possível repetir o nome de uma categoria, nem de um produto dentro da mesma categoria

## Estrutura

```
src/main/java/br/com/fatec/bazar
├── controller   # rotas da API
├── service      # regras de negócio
├── repository   # acesso ao banco de dados
├── model        # entidades (Categoria, Produto, Venda)
├── exception    # tratamento de erros
└── config       # configuração do Swagger
```

Relacionamentos: **Produto → Categoria** e **Venda → Produto**.

## Como executar

### Pré-requisitos
- JDK 21
- PostgreSQL em execução

### 1. Clonar o repositório
```bash
git clone <url-do-repositorio>
cd bazar
```

### 2. Criar o banco de dados
```sql
CREATE DATABASE bazar;
```
As tabelas são criadas automaticamente na primeira execução.

### 3. Configurar o acesso ao banco
Defina a senha do PostgreSQL em uma variável de ambiente:

```bash
# Linux / macOS
export DB_PASSWORD=sua_senha

# Windows (PowerShell)
$env:DB_PASSWORD="sua_senha"
```

Por padrão a API usa o usuário `postgres` e o banco `localhost:5432/bazar`. Para alterar, use as variáveis `DB_USERNAME` e `DB_URL`.

### 4. Executar
```bash
./mvnw spring-boot:run
```
No Windows: `mvnw.cmd spring-boot:run`

Também é possível abrir o projeto no IntelliJ IDEA e executar a classe `BazarApplication`.

A API fica disponível em `http://localhost:8080`.

## Documentação (Swagger)

Com a API em execução, acesse:

**http://localhost:8080/documentacao**

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/categorias` | Cadastrar categoria |
| GET | `/categorias` | Listar categorias |
| GET | `/categorias/{id}` | Buscar categoria por id |
| PUT | `/categorias/{id}` | Atualizar categoria |
| DELETE | `/categorias/{id}` | Excluir categoria |
| POST | `/produtos` | Cadastrar produto |
| GET | `/produtos` | Listar produtos |
| GET | `/produtos/{id}` | Buscar produto por id |
| GET | `/produtos/partenome/{nome}` | Buscar produtos por parte do nome |
| GET | `/produtos/categoria/{idCategoria}` | Listar produtos de uma categoria |
| PUT | `/produtos/{id}` | Atualizar produto |
| DELETE | `/produtos/{id}` | Excluir produto |
| POST | `/vendas` | Registrar venda |
| GET | `/vendas` | Listar vendas |
| GET | `/vendas/{id}` | Buscar venda por id |
| DELETE | `/vendas/{id}` | Cancelar venda |

Códigos de retorno: `200` sucesso · `201` criado · `204` sem conteúdo · `400` dados inválidos · `404` não encontrado · `409` conflito com regra de negócio.

## Exemplos de uso

Cadastre nesta ordem: **categoria → produto → venda**.

**POST /categorias**
```json
{
  "nome": "Roupas",
  "descricao": "Roupas usadas em bom estado"
}
```

**POST /produtos**
```json
{
  "nome": "Jaqueta jeans",
  "descricao": "Tamanho M",
  "preco": 35.90,
  "quantidadeEstoque": 10,
  "categoria": { "id": 1 }
}
```

**POST /vendas**
```json
{
  "produto": { "id": 1 },
  "quantidade": 2
}
```
Resposta: venda com `valorTotal` de `71.80`, e o estoque do produto passa de 10 para 8.

**Exemplo de erro (409)**
```json
{
  "timestamp": "2026-11-09T22:00:00",
  "status": 409,
  "mensagem": "Estoque insuficiente. Disponível: 8, solicitado: 9999.",
  "detalhes": []
}
```

## Autores

Projeto desenvolvido pelo grupo: **Gabriel Freitas Duarte**, **Tâmily Porto Martins** e **Luan Massuia Iglesias**.
