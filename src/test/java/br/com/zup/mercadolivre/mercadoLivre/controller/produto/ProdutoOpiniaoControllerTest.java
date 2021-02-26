package br.com.zup.mercadolivre.mercadoLivre.controller.produto;

import br.com.zup.mercadolivre.mercadoLivre.model.Caracteristica;
import br.com.zup.mercadolivre.mercadoLivre.model.Categoria;
import br.com.zup.mercadolivre.mercadoLivre.model.Opiniao;
import br.com.zup.mercadolivre.mercadoLivre.model.Produto;
import br.com.zup.mercadolivre.mercadoLivre.model.request.OpiniaoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static br.com.zup.mercadolivre.mercadoLivre.utils.CaracteristicasHelper.geraCaracteristicas;
import static br.com.zup.mercadolivre.mercadoLivre.utils.MockMvcRequest.performPost;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@Transactional
public class ProdutoOpiniaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager manager;

    @Autowired
    ObjectMapper objectMapper;

    private Produto produto;

    @BeforeEach
    public void before() {
        Categoria categoriaMae = new Categoria("testeCategoriaMae");
        manager.persist(categoriaMae);

        List<Caracteristica> caracteristicas = geraCaracteristicas(3);

        this.produto = new Produto("Mouse", 10.5, 5, caracteristicas,
                "Um produto top", categoriaMae);

        manager.persist(produto);
    }

    @Test
    @WithUserDetails(value = "teste@logado.com")
    public void deveriaCadastrarUmaOpiniaoSobreUmProduto() throws Exception {
        OpiniaoRequest request = new OpiniaoRequest(5, "Titulo opiniao", "Descricao opiniao");

        MvcResult result = performPost(mockMvc, "/produtos/"+produto.getId()+"/opinioes", 200, objectMapper, request);

        List<Opiniao> opinioes = manager.createQuery("SELECT o FROM Opiniao o", Opiniao.class).getResultList();

        assertTrue(opinioes.size() == 1);

        Opiniao opiniao = opinioes.get(0);

        assertAll(
                () -> assertEquals(request.getDescricao(), opiniao.getDescricao()),
                () -> assertEquals(request.getTitulo(), opiniao.getTitulo()),
                () -> assertEquals(request.getNota(), opiniao.getNota())
        );
    }

    @Test
    @WithUserDetails(value = "teste@logado.com")
    public void naoDeveriaCadastrarOpiniaoSemTitulo() throws Exception {
        OpiniaoRequest request = new OpiniaoRequest(5, "", "Descricao opiniao");

        MvcResult result = performPost(mockMvc, "/produtos/"+produto.getId()+"/opinioes", 400, objectMapper, request);

        List<Opiniao> opinioes = manager.createQuery("SELECT o FROM Opiniao o", Opiniao.class).getResultList();

        assertTrue(opinioes.size() == 0);
    }

    @Test
    @WithUserDetails(value = "teste@logado.com")
    public void naoDeveriaCriarUmaOpiniaoSemDescricao() throws Exception {
        OpiniaoRequest request = new OpiniaoRequest(5, "Titulo opiniao", "");

        MvcResult result = performPost(mockMvc, "/produtos/"+produto.getId()+"/opinioes", 400, objectMapper, request);

        List<Opiniao> opinioes = manager.createQuery("SELECT o FROM Opiniao o", Opiniao.class).getResultList();

        assertTrue(opinioes.size() == 0);
    }

    @Test
    @WithUserDetails(value = "teste@logado.com")
    public void naoDeveriaCadastrarOpiniaoComNotaMaiorQue5() throws Exception {
        OpiniaoRequest request = new OpiniaoRequest(6, "Titulo opiniao", "Descricao opiniao");

        MvcResult result = performPost(mockMvc, "/produtos/"+produto.getId()+"/opinioes", 400, objectMapper, request);

        List<Opiniao> opinioes = manager.createQuery("SELECT o FROM Opiniao o", Opiniao.class).getResultList();

        assertTrue(opinioes.size() == 0);
    }

    @Test
    @WithUserDetails(value = "teste@logado.com")
    public void naoDeveriaCadastrarOpiniaoComNotaMenorQue1() throws Exception {
        OpiniaoRequest request = new OpiniaoRequest(0, "Titulo opiniao", "Descricao opiniao");

        MvcResult result = performPost(mockMvc, "/produtos/"+produto.getId()+"/opinioes", 400, objectMapper, request);

        List<Opiniao> opinioes = manager.createQuery("SELECT o FROM Opiniao o", Opiniao.class).getResultList();

        assertTrue(opinioes.size() == 0);
    }
}
