package br.com.fatec.bazar.service;

import br.com.fatec.bazar.exception.RecursoNaoEncontradoException;
import br.com.fatec.bazar.exception.RegraNegocioException;
import br.com.fatec.bazar.exception.RequisicaoInvalidaException;
import br.com.fatec.bazar.model.Produto;
import br.com.fatec.bazar.model.Venda;
import br.com.fatec.bazar.repository.ProdutoRepository;
import br.com.fatec.bazar.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public Venda registrarVenda(Venda venda) {
        if (venda.getProduto() == null || venda.getProduto().getId() == null) {
            throw new RequisicaoInvalidaException("Informe o id do produto: {\"produto\": {\"id\": 1}}.");
        }
        Long idProduto = venda.getProduto().getId();
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto " + idProduto + " não encontrado."));

        // REGRA DE NEGÓCIO 1: não permite vender mais do que o estoque disponível
        if (produto.getQuantidadeEstoque() < venda.getQuantidade()) {
            throw new RegraNegocioException("Estoque insuficiente. Disponível: "
                    + produto.getQuantidadeEstoque() + ", solicitado: " + venda.getQuantidade() + ".");
        }

        // REGRA DE NEGÓCIO 2: baixa automática no estoque
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - venda.getQuantidade());
        produtoRepository.save(produto);

        // REGRA DE NEGÓCIO 3: valor total calculado automaticamente (preço x quantidade)
        venda.setId(null); // garante que será um cadastro novo
        venda.setProduto(produto);
        venda.setValorTotal(produto.getPreco().multiply(BigDecimal.valueOf(venda.getQuantidade())));
        venda.setDataVenda(LocalDateTime.now());
        return vendaRepository.save(venda);
    }

    public List<Venda> listarVendas() {
        return vendaRepository.findAll();
    }

    public Venda pesquisarVenda(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Venda " + id + " não encontrada."));
    }

    // REGRA DE NEGÓCIO 4: cancelar uma venda devolve os itens ao estoque
    @Transactional
    public void cancelarVenda(Long id) {
        Venda venda = pesquisarVenda(id);

        Produto produto = venda.getProduto();
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + venda.getQuantidade());
        produtoRepository.save(produto);

        vendaRepository.delete(venda);
    }
}
