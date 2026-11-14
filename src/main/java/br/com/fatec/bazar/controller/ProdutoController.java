package br.com.fatec.bazar.controller;

import br.com.fatec.bazar.exception.ErroResposta;
import br.com.fatec.bazar.model.Produto;
import br.com.fatec.bazar.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Produtos", description = "Gerenciamento de produtos e estoque (CRUD completo). Exige categoria já cadastrada.")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private static final String EXEMPLO_PRODUTO = """
            {
              "nome": "Jaqueta jeans",
              "descricao": "Jaqueta jeans tamanho M, pouco usada",
              "preco": 35.90,
              "quantidadeEstoque": 10,
              "categoria": { "id": 1 }
            }""";

    @Autowired
    private ProdutoService produtoService;

    @Operation(
            summary = "Cadastrar produto",
            description = "Cadastra um produto vinculado a uma categoria JÁ cadastrada (informe apenas o id da categoria). "
                    + "Preço deve ser maior que zero e o estoque não pode ser negativo. "
                    + "Não permite dois produtos com o mesmo nome na mesma categoria.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = EXEMPLO_PRODUTO)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou categoria não informada",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "404", description = "Categoria informada não existe",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "409", description = "Produto duplicado na categoria",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@Valid @RequestBody Produto produto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.cadastrarProduto(produto));
    }

    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos cadastrados, com sua categoria e o estoque atual.")
    @ApiResponse(responseCode = "200", description = "Lista de produtos",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Produto.class))))
    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        return ResponseEntity.ok(produtoService.listarProdutos());
    }

    @Operation(summary = "Pesquisar produto por id", description = "Retorna um produto pelo seu identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Id inválido",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Produto> pesquisarProduto(
            @Parameter(description = "ID do produto", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(produtoService.pesquisarProduto(id));
    }

    @Operation(summary = "Pesquisar produtos por parte do nome",
            description = "Retorna os produtos cujo nome contém o texto informado (sem diferenciar maiúsculas/minúsculas).")
    @ApiResponse(responseCode = "200", description = "Lista de produtos encontrados (pode ser vazia)",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Produto.class))))
    @GetMapping("/partenome/{nome}")
    public ResponseEntity<List<Produto>> pesquisarProdutoPorParteDoNome(
            @Parameter(description = "Parte do nome do produto", example = "jaqueta", required = true) @PathVariable String nome) {
        return ResponseEntity.ok(produtoService.pesquisarProdutoPorParteDoNome(nome));
    }

    @Operation(summary = "Listar produtos de uma categoria", description = "Retorna os produtos que pertencem à categoria informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de produtos da categoria (pode ser vazia)",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Produto.class)))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<Produto>> pesquisarProdutoPorCategoria(
            @Parameter(description = "ID da categoria", example = "1", required = true) @PathVariable Long idCategoria) {
        return ResponseEntity.ok(produtoService.pesquisarProdutoPorCategoria(idCategoria));
    }

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza todos os dados de um produto existente (inclusive preço, estoque e categoria).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = EXEMPLO_PRODUTO)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado",
                    content = @Content(schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou categoria não informada",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "404", description = "Produto ou categoria não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "409", description = "Produto duplicado na categoria",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(
            @Parameter(description = "ID do produto", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.atualizarProduto(id, produto));
    }

    @Operation(summary = "Excluir produto",
            description = "Exclui um produto. Não é permitido excluir se já existirem vendas registradas para ele.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto excluído", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "409", description = "Produto possui vendas",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(
            @Parameter(description = "ID do produto", example = "1", required = true) @PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
