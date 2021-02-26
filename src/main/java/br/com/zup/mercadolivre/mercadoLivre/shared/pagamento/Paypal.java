package br.com.zup.mercadolivre.mercadoLivre.shared.pagamento;

public class Paypal implements Pagamento{
    @Override
    public String geraLink(Long idCompra, String chave) {
        String linkRetorno = "localhost:8080/fechaCompra/"+ idCompra + "/"+ chave;
        //localhost:8080/fechaCompra/1/UUID

        String link  = "paypal.com/"+idCompra+"?redirectUrl=" + linkRetorno;

        return link;

    }
}
