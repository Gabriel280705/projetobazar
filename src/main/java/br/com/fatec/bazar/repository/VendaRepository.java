package br.com.fatec.bazar.repository;

import br.com.fatec.bazar.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    boolean existsByProdutoId(Long idProduto);
}
