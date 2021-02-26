package br.com.zup.mercadolivre.mercadoLivre.controller.produto;

import br.com.zup.mercadolivre.mercadoLivre.model.Pergunta;
import br.com.zup.mercadolivre.mercadoLivre.model.Produto;
import br.com.zup.mercadolivre.mercadoLivre.model.request.PerguntaRequest;
import br.com.zup.mercadolivre.mercadoLivre.model.response.PerguntaProdutoResponse;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoPerguntaController {

    @PersistenceContext
    private EntityManager manager;

    @PostMapping("/{id}/perguntas")
    @Transactional
    public ResponseEntity<List<PerguntaProdutoResponse>> adicionarPergunta(@PathVariable long id,
                                                                           @RequestBody @Valid PerguntaRequest form) throws NotFoundException {

        Produto produto = manager.find(Produto.class, id);

        Pergunta pergunta = form.toModel(produto);

        produto.adicionarPergunta(pergunta);

        manager.merge(produto);

        List<PerguntaProdutoResponse> perguntas = produto.todasPerguntasResponse();

        return ResponseEntity.ok(perguntas);

    }
}
