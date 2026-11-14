package br.com.fatec.bazar.exception;

/** Dados da requisição incompletos ou inválidos (ex.: referência sem id) -> HTTP 400 */
public class RequisicaoInvalidaException extends RuntimeException {
    public RequisicaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
