package br.com.zup.mercadolivre.mercadoLivre.model.request;

import br.com.zup.mercadolivre.mercadoLivre.model.Cliente;
import br.com.zup.mercadolivre.mercadoLivre.model.Pergunta;
import br.com.zup.mercadolivre.mercadoLivre.model.Produto;
import br.com.zup.mercadolivre.mercadoLivre.shared.ClienteLogado;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.constraints.NotBlank;

public class PerguntaRequest {

    @NotBlank
    private String titulo;

    public Pergunta toModel(Produto produto) {
        ClienteLogado logado = (ClienteLogado) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Cliente cliente = logado.get();
        return new Pergunta(titulo, produto, cliente);
    }

    @Deprecated
    public PerguntaRequest() {
    }

    public String getTitulo() {
        return titulo;
    }
}
