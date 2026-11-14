package br.com.fatec.bazar.service;

import br.com.fatec.bazar.exception.RecursoNaoEncontradoException;
import br.com.fatec.bazar.exception.RegraNegocioException;
import br.com.fatec.bazar.exception.RequisicaoInvalidaException;
import br.com.fatec.bazar.model.Categoria;
import br.com.fatec.bazar.model.Produto;
import br.com.fatec.bazar.repository.CategoriaRepository;
import br.com.fatec.bazar.repository.ProdutoRepository;
import br.com.fatec.bazar.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Transactional
    public Produto cadastrarProduto(Produto produto) {
        Categoria categoria = buscarCategoriaDoProduto(produto);

        // Regra: não pode haver produto com o mesmo nome na mesma categoria
        if (produtoRepository.existsByNomeIgnoreCaseAndCategoriaId(produto.getNome(), categoria.getId())) {
            throw new RegraNegocioException("Já existe o produto '" + produto.getNome() + "' nesta categoria.");
        }
        produto.setId(null); // garante que será um cadastro novo
        produto.setCategoria(categoria);
        return produtoRepository.save(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Produto pesquisarProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto " + id + " não encontrado."));
    }

    public List<Produto> pesquisarProdutoPorCategoria(Long idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new RecursoNaoEncontradoException("Categoria " + idCategoria + " não encontrada.");
        }
        return produtoRepository.findByCategoriaId(idCategoria);
    }

    public List<Produto> pesquisarProdutoPorParteDoNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional
    public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
        Produto produto = pesquisarProduto(id);
        Categoria categoria = buscarCategoriaDoProduto(produtoAtualizado);

        if (produtoRepository.existsByNomeIgnoreCaseAndCategoriaIdAndIdNot(produtoAtualizado.getNome(), categoria.getId(), id)) {
            throw new RegraNegocioException("Já existe o produto '" + produtoAtualizado.getNome() + "' nesta categoria.");
        }
        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
        produto.setCategoria(categoria);
        return produtoRepository.save(produto);
    }

    @Transactional
    public void deletarProduto(Long id) {
        Produto produto = pesquisarProduto(id);

        // Regra: não pode excluir produto que já possui vendas (preserva o histórico)
        if (vendaRepository.existsByProdutoId(id)) {
            throw new RegraNegocioException("Não é possível excluir o produto: existem vendas registradas para ele.");
        }
        produtoRepository.delete(produto);
    }

    // Confere se a categoria foi informada (com id) e se ela existe no banco
    private Categoria buscarCategoriaDoProduto(Produto produto) {
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            throw new RequisicaoInvalidaException("Informe o id da categoria: {\"categoria\": {\"id\": 1}}.");
        }
        Long idCategoria = produto.getCategoria().getId();
        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria " + idCategoria + " não encontrada."));
    }
}
