package br.com.zup.mercadolivre.mercadoLivre.controller.produto;

import br.com.zup.mercadolivre.mercadoLivre.model.*;
import br.com.zup.mercadolivre.mercadoLivre.model.request.CaracteristicaRequest;

import static br.com.zup.mercadolivre.mercadoLivre.utils.CaracteristicasHelper.geraCaracteristicas;
import static br.com.zup.mercadolivre.mercadoLivre.utils.CaracteristicasHelper.geraCaracteristicasRequest;
import static br.com.zup.mercadolivre.mercadoLivre.utils.MockMvcRequest.performGet;
import static br.com.zup.mercadolivre.mercadoLivre.utils.MockMvcRequest.performPost;
import static org.junit.jupiter.api.Assertions.*;

import br.com.zup.mercadolivre.mercadoLivre.model.request.ProdutoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@Transactional
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager manager;

    @Autowired
    ObjectMapper objectMapper;

    private Categoria categoriaMae;

    @BeforeEach
    public void before() {
        Cliente cliente = new Cliente("email@email.com", "123456");
        manager.persist(cliente);

        categoriaMae = new Categoria("testeCategoriaMae");
        manager.persist(categoriaMae);
    }

    @Test
    @WithUserDetails("teste@logado.com")
    public void deveriaPegarOsDetalhesDeUmProduto() throws Exception{
        List<Caracteristica> caracteristicas = geraCaracteristicas(3);

        Produto produto = new Produto("Mouse", 100, 5, caracteristicas,
                "Um produto top", categoriaMae);

        manager.persist(produto);

        MvcResult result = performGet(mockMvc, "/produtos/" + produto.getId(), 200, objectMapper, null);

        JSONObject json = new JSONObject(result.getResponse().getContentAsString());

        assertAll(
                () -> assertEquals(json.get("nome").toString(), produto.getNome()),
                () -> assertEquals(Double.parseDouble(json.get("valor").toString()), produto.getValor()),
                () -> assertEquals(Integer.parseInt(json.get("quantidade").toString()), produto.getQuantidade()),
                () -> assertEquals(json.get("descricao").toString(), produto.getDescricao()),
                () -> assertEquals(json.get("opinioes").toString(), "[]"),
                () -> assertEquals(json.get("perguntas").toString(), "[]")

        );

    }

    @Test
    @WithUserDetails("teste@logado.com")
    public void naoDeveriaPegarDetalhesDoProdutoComIdInvalido() throws Exception{
        List<Caracteristica> caracteristicas = geraCaracteristicas(3);

        Produto produto = new Produto("Mouse", 100, 5, caracteristicas,
                "Um produto top", categoriaMae);

        manager.persist(produto);

        performGet(mockMvc, "/produtos/" + produto.getId() + 1, 404, objectMapper, null);
    }

    @Test
    public void naoDeveriaCadastrarProdutoComUsuarioInexistente() throws Exception {
        List<CaracteristicaRequest> caracteristicas = geraCaracteristicasRequest(3);

        ProdutoRequest request = new ProdutoRequest("mouse", 200, 10,
                caracteristicas, "Descrição mouse", this.categoriaMae.getId());

        performPost(mockMvc, "/produtos", 403, objectMapper, request);

        List<Produto> produtos = manager.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();

        assertTrue(produtos.size() == 0);
    }

    @Test
    @WithMockUser("teste@logado.com")
    public void naoDeveriaCadastrarProdutoSemNome() throws Exception {
        List<CaracteristicaRequest> caracteristicas = geraCaracteristicasRequest(3);

        ProdutoRequest request = new ProdutoRequest("", 200, 10,
                caracteristicas, "Descrição mouse", this.categoriaMae.getId());

        performPost(mockMvc, "/produtos", 400, objectMapper, request);

        List<Produto> produtos = manager.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();

        assertTrue(produtos.size() == 0);
    }

    @Test
    @WithMockUser("teste@logado.com")
    public void naoDeveriaCadastrarProdutoComValorZerado() throws  Exception {
        List<CaracteristicaRequest> caracteristicas = geraCaracteristicasRequest(3);

        ProdutoRequest request = new ProdutoRequest("mouse", 0, 10,
                caracteristicas, "Descrição mouse", this.categoriaMae.getId());

        performPost(mockMvc, "/produtos", 400, objectMapper, request);

        List<Produto> produtos = manager.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();

        assertTrue(produtos.size() == 0);
    }

    @Test
    @WithMockUser("teste@logado.com")
    public void naoDeveriaCadastrarProdutoComQtdNegativa() throws Exception {
        List<CaracteristicaRequest> caracteristicas = geraCaracteristicasRequest(3);

        ProdutoRequest request = new ProdutoRequest("mouse", 100, -1,
                caracteristicas, "Descrição mouse", this.categoriaMae.getId());

        performPost(mockMvc, "/produtos", 400, objectMapper, request);

        List<Produto> produtos = manager.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();

        assertTrue(produtos.size() == 0);
    }
    @Test
    @WithUserDetails("teste@logado.com")
    public void naoDeveriaCadastrarProdutoSemCategoria() throws Exception {
        List<CaracteristicaRequest> caracteristicas = geraCaracteristicasRequest(3);

        ProdutoRequest request = new ProdutoRequest("mouse", 100, 0,
                caracteristicas, "Descrição mouse", this.categoriaMae.getId() + 1);

        performPost(mockMvc, "/produtos", 400, objectMapper, request);

        List<Produto> produtos = manager.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();

        assertTrue(produtos.size() == 0);
    }

    @Test
    @WithUserDetails("teste@logado.com")
    public void naoDeveriaCadastrarProdutoSemCaracteristicas() throws Exception {
        List<CaracteristicaRequest> caracteristicas = new ArrayList<CaracteristicaRequest>();

        ProdutoRequest request = new ProdutoRequest("mouse", 100, 0,
                caracteristicas, "Descrição mouse", this.categoriaMae.getId() + 1);

        performPost(mockMvc, "/produtos", 400, objectMapper, request);

        List<Produto> produtos = manager.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();

        assertTrue(produtos.size() == 0);
    }

    @Test
    @WithUserDetails("teste@logado.com")
    public void deveriaCadastrarUmNovoProduto() throws Exception {
        List<CaracteristicaRequest> caracteristicas = geraCaracteristicasRequest(3);

        ProdutoRequest request = new ProdutoRequest("mouse", 100, 1,
                caracteristicas, "Descrição mouse", this.categoriaMae.getId());

        performPost(mockMvc, "/produtos", 201, objectMapper, request);

        List<Produto> produtos = manager.createQuery("SELECT p FROM Produto p", Produto.class).getResultList();

        assertTrue(produtos.size() == 1);

        Produto produto = produtos.get(0);

        assertAll(
                () -> assertEquals(request.getDescricao(), produto.getDescricao()),
                () -> assertEquals(request.getNome(), produto.getNome()),
                () -> assertEquals(request.getIdCategoria(), produto.getCategoria().getId()),
                () -> assertEquals(request.getQuantidade(), produto.getQuantidade()),
                () -> assertEquals(request.getValor(), produto.getValor())
        );
    }
}
