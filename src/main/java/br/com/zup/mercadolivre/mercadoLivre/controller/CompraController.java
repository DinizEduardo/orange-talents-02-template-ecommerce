package br.com.zup.mercadolivre.mercadoLivre.controller;

import br.com.zup.mercadolivre.mercadoLivre.model.Compra;
import br.com.zup.mercadolivre.mercadoLivre.model.request.CompraRequest;
import br.com.zup.mercadolivre.mercadoLivre.shared.ClienteLogado;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/comprar")
public class CompraController {

    @PersistenceContext
    private EntityManager manager;

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrarCompra(@RequestBody @Valid CompraRequest form,
                                          @AuthenticationPrincipal ClienteLogado comprador,
                                          UriComponentsBuilder UriBuilder) throws URISyntaxException {

        Compra compra = form.toModel(manager, comprador.get());

        manager.persist(compra);

        compra.informaInteresse();

        URI uri = new URI(
                compra.getTipo().getPagamento().geraLink(
                        compra.getId(),
                        compra.getChave()));

        return ResponseEntity.status(302).location(uri).build();

    }

}
