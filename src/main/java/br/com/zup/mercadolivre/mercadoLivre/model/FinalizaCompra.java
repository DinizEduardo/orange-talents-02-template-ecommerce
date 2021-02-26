package br.com.zup.mercadolivre.mercadoLivre.model;

import br.com.zup.mercadolivre.mercadoLivre.shared.pagamento.StatusENUM;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "finaliza_compra")
public class FinalizaCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Compra compra;

    private String status;

    private String tipoPagamento;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    public FinalizaCompra(Compra compra, String status, String tipoPagamento) {
        this.compra = compra;
        this.status = status;
        this.tipoPagamento = tipoPagamento;
    }

    @Deprecated
    public FinalizaCompra() {
    }
}
