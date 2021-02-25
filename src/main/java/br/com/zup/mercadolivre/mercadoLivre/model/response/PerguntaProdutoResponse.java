package br.com.zup.mercadolivre.mercadoLivre.model.response;

import br.com.zup.mercadolivre.mercadoLivre.model.Pergunta;

import java.time.LocalDateTime;

public class PerguntaProdutoResponse {
    private String titulo;

    private ClienteResponse cliente;

    private LocalDateTime dataCriacao;

    public PerguntaProdutoResponse(Pergunta pergunta) {
        this.titulo = pergunta.getTitulo();
        this.cliente = new ClienteResponse(pergunta.getCliente());
        this.dataCriacao = pergunta.getDataCriacao();
    }


    public String getTitulo() {
        return titulo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public ClienteResponse getCliente() {
        return cliente;
    }
}
