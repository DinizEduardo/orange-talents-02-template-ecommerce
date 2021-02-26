package br.com.zup.mercadolivre.mercadoLivre.shared.pagamento;

public enum TipoPagamentoENUM {

    PAYPAL(new Paypal()),
    PAGSEGURO(new PagSeguro());

    Pagamento pagamento;

    TipoPagamentoENUM(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }
}
