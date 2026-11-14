package br.com.fatec.bazar.exception;

/** Violação de regra de negócio (ex.: estoque insuficiente) -> HTTP 409 */
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
