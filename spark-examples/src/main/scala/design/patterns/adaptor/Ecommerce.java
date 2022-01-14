package design.patterns.adaptor;

public class Ecommerce implements Business{

    private Buyer buyer = new Buyer();
    private Seller seller =  new Seller();

    public void buy() {
        buyer.buy();
    }

    public void sell() {
        seller.sell();
    }
}
