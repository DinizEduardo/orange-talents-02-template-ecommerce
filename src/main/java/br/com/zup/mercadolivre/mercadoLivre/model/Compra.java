package br.com.zup.mercadolivre.mercadoLivre.model;

import br.com.zup.mercadolivre.mercadoLivre.shared.pagamento.StatusENUM;
import br.com.zup.mercadolivre.mercadoLivre.shared.pagamento.TipoPagamentoENUM;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "compras")
public class Compra {

//    idCliente | idProduto | qtd | tipo (Paypal/Pagseguro) | Status | chave | dataCriacao

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    @NotNull
    private Integer qtd;

    @ManyToOne
    @NotNull
    private Produto produto;

    public Cliente getComprador() {
        return comprador;
    }

    @ManyToOne
    @NotNull
    private Cliente comprador;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private TipoPagamentoENUM tipo;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private StatusENUM status;

    @NotBlank
    private String chave = UUID.randomUUID().toString();

    @NotNull
    private LocalDateTime dataCriacao = LocalDateTime.now();

    public Compra(@Positive @NotNull Integer qtd, @NotNull Produto produto, @NotNull Cliente comprador, @NotNull TipoPagamentoENUM tipo, @NotNull StatusENUM status) {
        this.qtd = qtd;
        this.produto = produto;
        this.comprador = comprador;
        this.tipo = tipo;
        this.status = status;
    }

    @Deprecated
    public Compra() {
    }

    public Long getId() {
        return id;
    }

    public Integer getQtd() {
        return qtd;
    }

    public Produto getProduto() {
        return produto;
    }

    public TipoPagamentoENUM getTipo() {
        return tipo;
    }

    public StatusENUM getStatus() {
        return status;
    }

    public String getChave() {
        return chave;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", qtd=" + qtd +
                ", produto=" + produto +
                ", tipo=" + tipo +
                ", status=" + status +
                ", chave='" + chave + '\'' +
                ", dataCriacao=" + dataCriacao +
                '}';
    }

    public void informaInteresse() {

        System.out.println("E-mail para -> " + getProduto().getCliente().getEmail());
        System.out.println("Mensagem -> Olá, o seu produto " + getProduto().getNome() + " foi vendido" +
                " para " + getComprador().getEmail());

    }

    public void setStatus(StatusENUM status) {
        this.status = status;
    }
}
