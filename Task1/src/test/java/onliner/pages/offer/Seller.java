package onliner.pages.offer;

import org.openqa.selenium.WebElement;

public class Seller {
    final WebElement refSeller;
    final String hrefSeller;
    public final Double price;

    public Seller(Double foundPrice, String hrefSeller, WebElement refSeller) {
        super();
        this.hrefSeller = hrefSeller;
        this.refSeller = refSeller;
        this.price = foundPrice;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "hrefSeller='" + hrefSeller + '\'' +
                ", price=" + price +
                '}';
    }
}
