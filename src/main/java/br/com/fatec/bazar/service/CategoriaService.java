package br.com.fatec.bazar.service;

import br.com.fatec.bazar.exception.RecursoNaoEncontradoException;
import br.com.fatec.bazar.exception.RegraNegocioException;
import br.com.fatec.bazar.model.Categoria;
import br.com.fatec.bazar.repository.CategoriaRepository;
import br.com.fatec.bazar.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public Categoria cadastrarCategoria(Categoria categoria) {
        // Regra: não pode haver duas categorias com o mesmo nome
        if (categoriaRepository.existsByNomeIgnoreCase(categoria.getNome())) {
            throw new RegraNegocioException("Já existe uma categoria com o nome '" + categoria.getNome() + "'.");
        }
        categoria.setId(null); // garante que será um cadastro novo
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria pesquisarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria " + id + " não encontrada."));
    }

    @Transactional
    public Categoria atualizarCategoria(Long id, Categoria categoriaAtualizada) {
        Categoria categoria = pesquisarCategoria(id);

        if (categoriaRepository.existsByNomeIgnoreCaseAndIdNot(categoriaAtualizada.getNome(), id)) {
            throw new RegraNegocioException("Já existe uma categoria com o nome '" + categoriaAtualizada.getNome() + "'.");
        }
        categoria.setNome(categoriaAtualizada.getNome());
        categoria.setDescricao(categoriaAtualizada.getDescricao());
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void deletarCategoria(Long id) {
        Categoria categoria = pesquisarCategoria(id);

        // Regra: não pode excluir categoria que ainda possui produtos
        if (produtoRepository.existsByCategoriaId(id)) {
            throw new RegraNegocioException("Não é possível excluir a categoria: existem produtos cadastrados nela.");
        }
        categoriaRepository.delete(categoria);
    }
}
