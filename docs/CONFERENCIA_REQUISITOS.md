# Conferência dos requisitos do PDF (Projeto Final – AOS, Fatec Jales)

Situações: **Concluído e verificado** · **Implementado, mas não testado** · **Pendente** · **Não aplicável**.

> Importante: o ambiente de desenvolvimento não tinha internet, Maven nem PostgreSQL. Por isso o projeto **não foi compilado nem executado**. Só é marcado "Concluído e verificado" o que pôde ser conferido por inspeção dos arquivos. Veja `RELATORIO_TESTES.md`.

| Requisito do PDF | Implementação realizada | Arquivo ou componente | Situação | Evidência ou pendência |
|---|---|---|---|---|
| 1. Arquitetura REST | Recursos `/categorias`, `/produtos`, `/vendas` com verbos HTTP e códigos de status adequados | `controller/*` | Implementado, mas não testado | Rodar a coleção Postman |
| 1. Camadas Controller, Service, Repository, Model | Pacotes separados, 3 classes em cada camada | `controller/`, `service/`, `repository/`, `model/` | Concluído e verificado | Estrutura de pastas conferida (19 arquivos .java) |
| 1. Métodos GET, POST, PUT, DELETE | Os 4 métodos usados nos 3 recursos (PUT só em categorias e produtos, pois venda não é editada, e sim cancelada) | `controller/*` | Implementado, mas não testado | Coleção Postman |
| 1. Endpoints claros e padronizados | `POST /x`, `GET /x`, `GET /x/{id}`, `PUT /x/{id}`, `DELETE /x/{id}` em todos os recursos | `controller/*` | Concluído e verificado | Lista de rotas conferida no código e no README |
| 1. JSON para envio e recebimento | `@RestController` + `@RequestBody`; erros também em JSON (`ErroResposta`) | `controller/*`, `exception/*` | Implementado, mas não testado | Postman |
| 2. Mínimo 3 entidades relacionadas | Categoria, Produto, Venda (`Produto→Categoria`, `Venda→Produto`) | `model/*` | Concluído e verificado | 3 `@Entity` e 2 `@ManyToOne` conferidos |
| 2. CRUD completo em pelo menos 2 entidades | CRUD completo em Categoria e Produto | `CategoriaController`, `ProdutoController` | Implementado, mas não testado | Pastas 1 e 2 da coleção |
| 2. Mínimo 8 endpoints | 16 endpoints | `controller/*` | Implementado, mas não testado | 16 mapeamentos contados no código; funcionamento depende de execução |
| 2. Pelo menos 2 regras de negócio | Estoque insuficiente (409); baixa de estoque + total automático; devolução ao cancelar; bloqueio de exclusão com dependentes; nomes duplicados | `service/*` | Implementado, mas não testado | `VendaServiceTest` (3 testes escritos, não executados) e pasta 4 da coleção |
| 2. Validação dos dados | Bean Validation nos models + `GlobalExceptionHandler` (400 com detalhes por campo) | `model/*`, `exception/*` | Implementado, mas não testado | Pasta 4 da coleção |
| 3. Banco relacional | PostgreSQL | `pom.xml`, `application.properties` | Implementado, mas não testado | Subir a API com o banco criado |
| 3. Persistir os dados | Spring Data JPA + `ddl-auto=update` | `repository/*`, `application.properties` | Implementado, mas não testado | Reiniciar a API e conferir que os dados permanecem |
| 3. Relacionamentos entre entidades | `@ManyToOne` com `@JoinColumn` e chaves estrangeiras | `model/Produto`, `model/Venda`, `sql/02-schema.sql` | Implementado, mas não testado | Tabelas criadas com FKs |
| 3. Integridade básica | Chaves primárias/estrangeiras, `UNIQUE`, `NOT NULL`, `CHECK` (no SQL), regras que impedem registros órfãos | `model/*`, `service/*`, `sql/02-schema.sql` | Implementado, mas não testado | Casos 409 da coleção |
| 4. Swagger/OpenAPI: descrição geral | `@OpenAPIDefinition` | `config/SwaggerConfig` | Implementado, mas não testado | Abrir `/documentacao` |
| 4. Descrição dos endpoints | `@Tag`, `@Operation` (summary + description) em todas as rotas | `controller/*` | Implementado, mas não testado | Swagger UI |
| 4. Parâmetros e dados esperados | `@Parameter` e `@Schema` nos campos dos models | `controller/*`, `model/*` | Implementado, mas não testado | Swagger UI |
| 4. Exemplos de requisição e resposta | `@ExampleObject` nos POST/PUT e `@Schema(example)` nos campos | `controller/*`, `model/*` | Implementado, mas não testado | Swagger UI |
| 4. Códigos de retorno | `@ApiResponses` com 200/201/204/400/404/409 em cada rota | `controller/*` | Implementado, mas não testado | Swagger UI |
| Entrega: `README.md` na página inicial do repositório | README com descrição, problema, funcionalidades, tecnologias e execução | `README.md` | Concluído e verificado | Arquivo presente na raiz do projeto |
| Entrega: repositório GitHub **público** com o código | Projeto pronto para ser enviado ao GitHub (`.gitignore` incluso) | – | Pendente | Ação do aluno: criar repositório público, enviar o código e entregar o link no Teams até 22/11/2026 23h59 |
| Apresentação oral (23 ou 30/11/2026) | – | – | Não aplicável | É feita pela dupla; o ambiente não pode realizá-la |

## Requisitos extras do pedido (fora do PDF)
| Item | Situação | Evidência ou pendência |
|---|---|---|
| `pom.xml` válido e dependências declaradas | Implementado, mas não testado | XML bem formado; `mvn` não pôde ser executado (sem internet) |
| Maven Wrapper | Concluído e verificado | `mvnw`, `mvnw.cmd`, `.mvn/` copiados do projeto original |
| Compilação | Pendente | Rodar `./mvnw clean package -DskipTests` com internet |
| Scripts SQL | Implementado, mas não testado | `sql/` (sintaxe PostgreSQL não executada) |
| Coleção Postman | Implementado, mas não testado | 30 requisições, JSON gerado por script |
| Projeto original preservado | Concluído e verificado | Nada foi alterado; só foi extraída uma cópia de trabalho |
