package br.com.zup.mercadolivre.mercadoLivre.model.request;

import br.com.zup.mercadolivre.mercadoLivre.model.Cliente;
import br.com.zup.mercadolivre.mercadoLivre.model.Compra;
import br.com.zup.mercadolivre.mercadoLivre.model.Produto;
import br.com.zup.mercadolivre.mercadoLivre.shared.ExistsId;
import br.com.zup.mercadolivre.mercadoLivre.shared.pagamento.StatusENUM;
import br.com.zup.mercadolivre.mercadoLivre.shared.pagamento.TipoPagamentoENUM;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CompraRequest {

    @ExistsId(domainClass = Produto.class, fieldName = "id")
    private Long idProduto;

    @Positive
    @NotNull
    private Integer qtd;

    @NotBlank
    private String tipoPagamento;

    public Long getIdProduto() {
        return idProduto;
    }

    public Integer getQtd() {
        return qtd;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public Compra toModel(EntityManager manager, Cliente comprador) {

        @NotNull Produto produto = manager.find(Produto.class, idProduto);

        if(produto == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "IdProduto não encontrado");

        if(produto.getCliente().getId() == comprador.getId())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não pode comprar o seu proprio produto!");

        return new Compra(qtd,
                produto,
                comprador,
                TipoPagamentoENUM.valueOf(tipoPagamento),
                StatusENUM.INICIADA);

    }
}
