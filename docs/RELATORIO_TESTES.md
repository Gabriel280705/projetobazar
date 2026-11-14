# Relatório de testes

## Limitações do ambiente de desenvolvimento
Sem acesso à internet, sem Maven, sem jars do Spring em cache e sem PostgreSQL. Consequências:
- **Não foi possível compilar** o projeto.
- **Não foi possível subir a aplicação**, testar o banco, o Swagger ou os endpoints.

## Testes efetivamente executados
| Verificação | Resultado |
|---|---|
| `pom.xml` é um XML bem formado | OK |
| Cada `.java` tem `package` igual à sua pasta, declara a classe/record/interface com o nome do arquivo e as chaves `{}` estão balanceadas (19 arquivos) | OK |
| Contagem de endpoints nos controllers | 16 (≥ 8 exigidos) |
| Rotas conferidas por leitura (padronização) | OK |
| JSON da coleção Postman gerado por script (30 requisições) | Gerado sem erro |

## Escritos, mas NÃO executados
| O que | Onde | Como executar |
|---|---|---|
| 3 testes unitários das regras de venda (estoque insuficiente, baixa de estoque + total, devolução no cancelamento) | `src/test/.../VendaServiceTest.java` | `./mvnw test` (não exigem banco) |
| 30 requisições Postman com verificação automática de status e de estoque | `postman/API_Bazar.postman_collection.json` | Postman > Run collection, com a API no ar |
| Verificação do Swagger | – | Abrir http://localhost:8080/documentacao |

> O teste `contextLoads` do projeto original foi **omitido** de propósito: ele exige PostgreSQL no ar e faria `mvn package` falhar sem banco.

## Primeira verificação recomendada
1. `./mvnw clean package` (compila e roda os 3 testes unitários).
2. Criar o banco `bazar`, definir `DB_PASSWORD` e rodar `./mvnw spring-boot:run`.
3. Abrir `/documentacao` e rodar a coleção Postman na ordem.

Se aparecer qualquer erro de compilação, copie a mensagem completa para que eu corrija.
