package br.com.fatec.bazar.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "venda")
@Schema(description = "Venda de um produto. O valor total e a data são calculados pela API.")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venda")
    @Schema(description = "Identificador único da venda (gerado automaticamente)", example = "1")
    private Long id;

    // Relacionamento: várias vendas podem ser do mesmo produto
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_produto", nullable = false)
    @NotNull(message = "O produto é obrigatório. Informe {\"produto\": {\"id\": 1}}.")
    @Schema(description = "Produto vendido. No cadastro basta informar o id: {\"id\": 1}")
    private Produto produto;

    @Column(nullable = false)
    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade vendida deve ser no mínimo 1.")
    @Schema(description = "Quantidade vendida", example = "2")
    private Integer quantidade;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Valor total (preço x quantidade), calculado automaticamente", example = "71.80",
            accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal valorTotal;

    @Column(name = "data_venda", nullable = false)
    @Schema(description = "Data e hora da venda, definida automaticamente",
            accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dataVenda;
}
