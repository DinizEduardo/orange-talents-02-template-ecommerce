package br.com.zup.mercadolivre.mercadoLivre.controller.produto;

import br.com.zup.mercadolivre.mercadoLivre.model.Cliente;
import br.com.zup.mercadolivre.mercadoLivre.model.Opiniao;
import br.com.zup.mercadolivre.mercadoLivre.model.Pergunta;
import br.com.zup.mercadolivre.mercadoLivre.model.Produto;
import br.com.zup.mercadolivre.mercadoLivre.model.request.NovasImagensRequest;
import br.com.zup.mercadolivre.mercadoLivre.model.request.OpiniaoRequest;
import br.com.zup.mercadolivre.mercadoLivre.model.request.PerguntaRequest;
import br.com.zup.mercadolivre.mercadoLivre.model.request.ProdutoRequest;
import br.com.zup.mercadolivre.mercadoLivre.model.response.OpiniaoProdutoResponse;
import br.com.zup.mercadolivre.mercadoLivre.model.response.PerguntaProdutoResponse;
import br.com.zup.mercadolivre.mercadoLivre.model.response.ProdutoResponse;
import br.com.zup.mercadolivre.mercadoLivre.utils.UploaderFake;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private UploaderFake uploaderFake;

    @PostMapping
    @Transactional
    public ResponseEntity<ProdutoResponse> cadastrar(@RequestBody @Valid ProdutoRequest form,
                                                     UriComponentsBuilder uriBuilder) {
        Produto produto = form.toModel(manager);

        manager.persist(produto);

        URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produto.getId()).toUri();

        return ResponseEntity.created(uri).body(new ProdutoResponse(produto));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<ProdutoResponse> buscar(@PathVariable long id) {
        Produto produto = manager.find(Produto.class, id);

        if(produto == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");

        return ResponseEntity.ok(new ProdutoResponse(produto));
    }

}