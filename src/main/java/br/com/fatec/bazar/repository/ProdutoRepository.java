package br.com.fatec.bazar.repository;

import br.com.fatec.bazar.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Query Methods
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByCategoriaId(Long idCategoria);

    boolean existsByCategoriaId(Long idCategoria);

    boolean existsByNomeIgnoreCaseAndCategoriaId(String nome, Long idCategoria);

    boolean existsByNomeIgnoreCaseAndCategoriaIdAndIdNot(String nome, Long idCategoria, Long id);
}
