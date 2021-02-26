package br.com.zup.mercadolivre.mercadoLivre.model.request;

import br.com.zup.mercadolivre.mercadoLivre.model.Cliente;
import br.com.zup.mercadolivre.mercadoLivre.model.Pergunta;
import br.com.zup.mercadolivre.mercadoLivre.model.Produto;
import br.com.zup.mercadolivre.mercadoLivre.shared.ClienteLogado;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PerguntaRequest {

    @NotBlank
    private String titulo;

    public Pergunta toModel(@NotNull @Valid Produto produto) {
        if(produto == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");

        ClienteLogado logado = (ClienteLogado) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Cliente cliente = logado.get();
        return new Pergunta(titulo, produto, cliente);
    }

    @Deprecated
    public PerguntaRequest() {
    }

    public PerguntaRequest(@NotBlank String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }
}
