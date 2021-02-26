package br.com.zup.mercadolivre.mercadoLivre.shared.pagamento;

public interface Pagamento {

    public String geraLink(Long idCompra, String chave);

}
