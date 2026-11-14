package br.com.fatec.bazar.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Formato padrão das respostas de erro da API")
public record ErroResposta(
        @Schema(description = "Momento do erro") LocalDateTime timestamp,
        @Schema(description = "Código HTTP", example = "409") int status,
        @Schema(description = "Mensagem principal", example = "Estoque insuficiente. Disponível: 3, solicitado: 5.") String mensagem,
        @Schema(description = "Detalhes (ex.: erros de validação por campo)") List<String> detalhes) {
}
