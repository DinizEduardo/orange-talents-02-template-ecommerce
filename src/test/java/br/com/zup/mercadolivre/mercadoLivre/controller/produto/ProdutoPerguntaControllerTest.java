package br.com.zup.mercadolivre.mercadoLivre.controller.produto;

import br.com.zup.mercadolivre.mercadoLivre.model.*;
import br.com.zup.mercadolivre.mercadoLivre.model.request.PerguntaRequest;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static br.com.zup.mercadolivre.mercadoLivre.utils.CaracteristicasHelper.geraCaracteristicas;
import static br.com.zup.mercadolivre.mercadoLivre.utils.MockMvcRequest.performPost;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@Transactional
public class ProdutoPerguntaControllerTest {

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
    @WithUserDetails("teste@logado.com")
    public void deveriaCadastrarUmaPerguntaSobreUmProduto() throws Exception {
        PerguntaRequest request = new PerguntaRequest("O produto é fragil?");

        performPost(mockMvc, "/produtos/"+ produto.getId() +"/perguntas", 200, objectMapper, request);

        List<Pergunta> perguntas = manager.createQuery("SELECT p FROM Pergunta p", Pergunta.class).getResultList();

        assertTrue(perguntas.size() == 1);

        Pergunta pergunta = perguntas.get(0);

        assertAll(
                () -> assertEquals(request.getTitulo(), pergunta.getTitulo())
        );
    }

    @Test
    @WithUserDetails("teste@logado.com")
    public void naoDeveriaCadastrarPerguntaEmProdutoQueNaoExiste() throws Exception {
        PerguntaRequest request = new PerguntaRequest("O produto é fragil?");

        performPost(mockMvc, "/produtos/"+ produto.getId()+1 +"/perguntas", 404, objectMapper, request);

        List<Pergunta> perguntas = manager.createQuery("SELECT p FROM Pergunta p", Pergunta.class).getResultList();

        assertTrue(perguntas.size() == 0);
    }

    @Test
    @WithUserDetails("teste@logado.com")
    public void naoDeveriaCadastrarPerguntaSemTitulo() throws Exception{
        PerguntaRequest request = new PerguntaRequest("");

        performPost(mockMvc, "/produtos/"+ produto.getId() +"/perguntas", 400, objectMapper, request);

        List<Pergunta> perguntas = manager.createQuery("SELECT p FROM Pergunta p", Pergunta.class).getResultList();

        assertTrue(perguntas.size() == 0);
    }


}
