package br.com.fatec.bazar.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "produto",
        uniqueConstraints = @UniqueConstraint(name = "uk_produto_nome_categoria",
                columnNames = {"nome_produto", "id_categoria"}))
@Schema(description = "Produto à venda no bazar. Pertence a uma categoria já cadastrada.")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    @Schema(description = "Identificador único do produto (gerado automaticamente)", example = "1")
    private Long id;

    @Column(name = "nome_produto", nullable = false, length = 100)
    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome do produto deve ter entre 3 e 100 caracteres.")
    @Schema(description = "Nome do produto", example = "Jaqueta jeans")
    private String nome;

    @Column(name = "descricao_produto", length = 255)
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    @Schema(description = "Descrição do produto", example = "Jaqueta jeans tamanho M, pouco usada")
    private String descricao;

    @Column(name = "preco_produto", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser maior que zero.")
    @Schema(description = "Preço unitário em reais", example = "35.90")
    private BigDecimal preco;

    @Column(name = "quantidade_estoque", nullable = false)
    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    @Schema(description = "Quantidade disponível em estoque", example = "10")
    private Integer quantidadeEstoque;

    // Relacionamento: vários produtos pertencem a uma categoria
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    @NotNull(message = "A categoria é obrigatória. Informe {\"categoria\": {\"id\": 1}}.")
    @Schema(description = "Categoria do produto. No cadastro/atualização basta informar o id: {\"id\": 1}")
    private Categoria categoria;
}
