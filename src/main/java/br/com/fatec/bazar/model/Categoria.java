package br.com.fatec.bazar.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "categoria")
@Schema(description = "Categoria de produtos do bazar (ex.: Roupas, Livros)")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    @Schema(description = "Identificador único da categoria (gerado automaticamente)", example = "1")
    private Long id;

    @Column(name = "nome_categoria", nullable = false, unique = true, length = 50)
    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Size(min = 3, max = 50, message = "O nome da categoria deve ter entre 3 e 50 caracteres.")
    @Schema(description = "Nome da categoria (único)", example = "Roupas")
    private String nome;

    @Column(name = "descricao_categoria", length = 200)
    @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres.")
    @Schema(description = "Descrição da categoria", example = "Roupas usadas em bom estado")
    private String descricao;
}
