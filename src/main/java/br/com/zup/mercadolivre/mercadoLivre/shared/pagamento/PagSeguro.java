package br.com.zup.mercadolivre.mercadoLivre.shared.pagamento;

public class PagSeguro implements Pagamento {
    @Override
    public String geraLink(Long idCompra, String chave) {

        String linkRetorno = "localhost:8080/fechaCompra/"+ idCompra + "/"+ chave;

        String link = "pagseguro.com?returnId="+idCompra+"&redirectUrl=" + linkRetorno;

        return link;
    }
}
