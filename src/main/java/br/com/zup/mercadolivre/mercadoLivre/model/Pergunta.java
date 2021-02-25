package br.com.zup.mercadolivre.mercadoLivre.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "perguntas_produto")
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @ManyToOne
    private Produto produto;

    @ManyToOne
    private Cliente cliente;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    public Pergunta(String titulo, Produto produto, Cliente cliente) {
        this.titulo = titulo;
        this.produto = produto;
        this.cliente = cliente;
    }

    @Deprecated
    public Pergunta() {
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Produto getProduto() {
        return produto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
