package br.com.zup.mercadolivre.mercadoLivre.controller.produto;

import br.com.zup.mercadolivre.mercadoLivre.model.Produto;
import br.com.zup.mercadolivre.mercadoLivre.model.request.NovasImagensRequest;
import br.com.zup.mercadolivre.mercadoLivre.model.response.ProdutoResponse;
import br.com.zup.mercadolivre.mercadoLivre.utils.UploaderFake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/produtos")
public class ProdutoImagemController {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private UploaderFake uploaderFake;

    @PostMapping("/{id}/imagem")
    @Transactional
    public ResponseEntity<ProdutoResponse> adicionarImagens(@PathVariable("id") Long id,
                                                            @Valid NovasImagensRequest form) {

        Produto produto = manager.find(Produto.class, id);

        if(!produto.pertenceAoClienteLogado()) {
            return ResponseEntity.badRequest().build();
        }

        Set<String> links = uploaderFake.envia(form.getImagens());

        produto.associaImagens(links);

        manager.merge(produto);

        return ResponseEntity.ok(new ProdutoResponse(produto));
    }
}
