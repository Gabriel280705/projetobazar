package br.com.fatec.bazar.controller;

import br.com.fatec.bazar.exception.ErroResposta;
import br.com.fatec.bazar.model.Venda;
import br.com.fatec.bazar.service.VendaService;
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

@Tag(name = "Vendas", description = "Registro de vendas com baixa automática de estoque")
@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @Operation(
            summary = "Registrar venda",
            description = "Registra a venda de um produto JÁ cadastrado. Regras: (1) a quantidade não pode exceder o estoque; "
                    + "(2) o estoque do produto é reduzido automaticamente; (3) o valor total (preço x quantidade) e a data "
                    + "são calculados pela API - não precisam ser enviados.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"produto\": {\"id\": 1}, \"quantidade\": 2}")))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venda registrada com sucesso",
                    content = @Content(schema = @Schema(implementation = Venda.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex.: quantidade menor que 1, produto não informado)",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "409", description = "Estoque insuficiente",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @PostMapping
    public ResponseEntity<Venda> registrarVenda(@Valid @RequestBody Venda venda) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.registrarVenda(venda));
    }

    @Operation(summary = "Listar vendas", description = "Retorna todas as vendas registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de vendas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Venda.class))))
    @GetMapping
    public ResponseEntity<List<Venda>> listarVendas() {
        return ResponseEntity.ok(vendaService.listarVendas());
    }

    @Operation(summary = "Pesquisar venda por id", description = "Retorna uma venda pelo seu identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda encontrada",
                    content = @Content(schema = @Schema(implementation = Venda.class))),
            @ApiResponse(responseCode = "400", description = "Id inválido",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venda> pesquisarVenda(
            @Parameter(description = "ID da venda", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(vendaService.pesquisarVenda(id));
    }

    @Operation(summary = "Cancelar venda",
            description = "Cancela (remove) uma venda e devolve a quantidade vendida ao estoque do produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Venda cancelada e estoque restaurado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarVenda(
            @Parameter(description = "ID da venda", example = "1", required = true) @PathVariable Long id) {
        vendaService.cancelarVenda(id);
        return ResponseEntity.noContent().build();
    }
}
