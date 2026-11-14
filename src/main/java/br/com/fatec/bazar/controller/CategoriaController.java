package br.com.fatec.bazar.controller;

import br.com.fatec.bazar.exception.ErroResposta;
import br.com.fatec.bazar.model.Categoria;
import br.com.fatec.bazar.service.CategoriaService;
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

@Tag(name = "Categorias", description = "Gerenciamento de categorias de produtos (CRUD completo)")
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(
            summary = "Cadastrar categoria",
            description = "Cadastra uma nova categoria. O nome é obrigatório (3 a 50 caracteres) e não pode se repetir.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"nome\": \"Roupas\", \"descricao\": \"Roupas usadas em bom estado\"}")))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex.: nome em branco)",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "409", description = "Já existe categoria com esse nome",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @PostMapping
    public ResponseEntity<Categoria> cadastrarCategoria(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.cadastrarCategoria(categoria));
    }

    @Operation(summary = "Listar categorias", description = "Retorna todas as categorias cadastradas (lista vazia se não houver nenhuma).")
    @ApiResponse(responseCode = "200", description = "Lista de categorias",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Categoria.class))))
    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @Operation(summary = "Pesquisar categoria por id", description = "Retorna uma categoria pelo seu identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada",
                    content = @Content(schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Id inválido",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> pesquisarCategoria(
            @Parameter(description = "ID da categoria", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.pesquisarCategoria(id));
    }

    @Operation(
            summary = "Atualizar categoria",
            description = "Atualiza nome e descrição de uma categoria existente.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"nome\": \"Roupas e Acessórios\", \"descricao\": \"Roupas, bolsas e cintos\"}")))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada",
                    content = @Content(schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "409", description = "Já existe outra categoria com esse nome",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizarCategoria(
            @Parameter(description = "ID da categoria", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.atualizarCategoria(id, categoria));
    }

    @Operation(summary = "Excluir categoria",
            description = "Exclui uma categoria. Não é permitido excluir se houver produtos cadastrados nela.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria excluída", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "409", description = "Categoria possui produtos",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoria(
            @Parameter(description = "ID da categoria", example = "1", required = true) @PathVariable Long id) {
        categoriaService.deletarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
