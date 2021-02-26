package br.com.zup.mercadolivre.mercadoLivre.controller.produto;

import br.com.zup.mercadolivre.mercadoLivre.model.Opiniao;
import br.com.zup.mercadolivre.mercadoLivre.model.Produto;
import br.com.zup.mercadolivre.mercadoLivre.model.request.OpiniaoRequest;
import br.com.zup.mercadolivre.mercadoLivre.model.response.OpiniaoProdutoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@RestController
@RequestMapping("/produtos")
public class ProdutoOpiniaoController {

    @PersistenceContext
    private EntityManager manager;

    @PostMapping("/{id}/opinioes")
    @Transactional
    public ResponseEntity<OpiniaoProdutoResponse> adicionarOpiniao(@PathVariable long id,
                                                                   @RequestBody @Valid OpiniaoRequest form) {

        Produto produto = manager.find(Produto.class, id);

        Opiniao opiniao = form.toModel(manager, produto);

        produto.adicionaOpiniao(opiniao);

        manager.merge(produto);

        return ResponseEntity.ok(new OpiniaoProdutoResponse(opiniao));
    }

}
