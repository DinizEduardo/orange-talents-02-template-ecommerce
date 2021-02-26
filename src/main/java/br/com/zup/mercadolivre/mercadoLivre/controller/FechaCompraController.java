package br.com.zup.mercadolivre.mercadoLivre.controller;

import br.com.zup.mercadolivre.mercadoLivre.model.Compra;
import br.com.zup.mercadolivre.mercadoLivre.model.FinalizaCompra;
import br.com.zup.mercadolivre.mercadoLivre.model.request.FinalizaCompraRequest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@RequestMapping("/fechaCompra")
public class FechaCompraController {

    @PersistenceContext
    private EntityManager manager;

    @PostMapping("/{idCompra}/{chave}")
    @Transactional
    public void finalizaCompra(@PathVariable Long idCompra,
                               @PathVariable String chave) {

        FinalizaCompraRequest request = new FinalizaCompraRequest(idCompra, "PAYPAL",chave);

        FinalizaCompra finalizada = request.toModel(manager);

        manager.merge(finalizada);
    }

}
