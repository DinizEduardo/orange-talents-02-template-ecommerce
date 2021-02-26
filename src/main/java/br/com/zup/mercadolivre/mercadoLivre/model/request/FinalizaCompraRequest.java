package br.com.zup.mercadolivre.mercadoLivre.model.request;

import br.com.zup.mercadolivre.mercadoLivre.model.Compra;
import br.com.zup.mercadolivre.mercadoLivre.model.FinalizaCompra;
import br.com.zup.mercadolivre.mercadoLivre.shared.pagamento.StatusENUM;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class FinalizaCompraRequest {

    @NotNull
    private Long idCompra;

    @NotBlank
    private String tipoPagamento;

    private String chave;

    public FinalizaCompraRequest(@NotNull Long idCompra,
                                 @NotBlank String tipoPagamento,
                                 String chave) {
        this.idCompra = idCompra;
        this.tipoPagamento = tipoPagamento;
        this.chave = chave;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public String getChave() {
        return chave;
    }

    public FinalizaCompra toModel(EntityManager manager) {
        Compra compra = manager.find(Compra.class, this.idCompra);

        if(compra.getStatus() == StatusENUM.FINALIZADA || !compra.getChave().equals(this.chave.trim()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi possivel encontrar a compra com os dados passados");

        compra.setStatus(StatusENUM.FINALIZADA);

        return new FinalizaCompra(compra,
                compra.getStatus().toString(),
                compra.getTipo().toString());

    }
}
