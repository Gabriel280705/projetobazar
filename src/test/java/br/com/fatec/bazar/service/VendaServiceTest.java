package br.com.fatec.bazar.service;

import br.com.fatec.bazar.exception.RegraNegocioException;
import br.com.fatec.bazar.model.Produto;
import br.com.fatec.bazar.model.Venda;
import br.com.fatec.bazar.repository.ProdutoRepository;
import br.com.fatec.bazar.repository.VendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Testes unitários das regras de negócio de venda (não precisam de banco de dados). */
@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private VendaService vendaService;

    private Produto produto(int estoque) {
        Produto p = new Produto();
        p.setId(1L);
        p.setNome("Jaqueta jeans");
        p.setPreco(new BigDecimal("35.90"));
        p.setQuantidadeEstoque(estoque);
        return p;
    }

    private Venda pedido(Produto p, int quantidade) {
        Produto ref = new Produto();
        ref.setId(p.getId());
        Venda v = new Venda();
        v.setProduto(ref);
        v.setQuantidade(quantidade);
        return v;
    }

    @Test
    void naoPermiteVendaSemEstoqueSuficiente() {
        Produto p = produto(3);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(p));

        assertThrows(RegraNegocioException.class, () -> vendaService.registrarVenda(pedido(p, 5)));

        assertEquals(3, p.getQuantidadeEstoque());
        verify(vendaRepository, never()).save(any());
    }

    @Test
    void vendaBaixaEstoqueECalculaTotal() {
        Produto p = produto(10);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(p));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(inv -> inv.getArgument(0));

        Venda venda = vendaService.registrarVenda(pedido(p, 2));

        assertEquals(8, p.getQuantidadeEstoque());
        assertEquals(new BigDecimal("71.80"), venda.getValorTotal());
    }

    @Test
    void cancelarVendaDevolveEstoque() {
        Produto p = produto(8);
        Venda venda = pedido(p, 2);
        venda.setProduto(p);
        when(vendaRepository.findById(5L)).thenReturn(Optional.of(venda));

        vendaService.cancelarVenda(5L);

        assertEquals(10, p.getQuantidadeEstoque());
        verify(vendaRepository).delete(venda);
    }
}
