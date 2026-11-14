package br.com.fatec.bazar.exception;

/** Registro não encontrado -> HTTP 404 */
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
