package pages.offer;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.BasePage;
import pages.SellerPage;
import tools.Product;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class OfferPage extends BasePage {
    public OfferPage(@Nonnull WebDriver driver) {
        super(driver);
    }

    public Product product;

    public SellerPage navigateToSeller(Seller seller) {
        seller.refSeller.click();
        return new SellerPage(webDriver);
    }

    public List<Seller> getSellers() {
        List<Seller> listSellers = new ArrayList<Seller>();
        List<WebElement> elements = webDriver.findElements(By.xpath("//tr[@class=\"marked state_add-to-cart m-divider\"]"));
        for (WebElement item : elements) {
            WebElement foundRefSeller = item.findElement(By.xpath("./td[@class=\"b-cell-4\"]/div[@class=\"b-cell-4__line-1\"]/a[@class=\"logo\"]/img"));
            Double foundPrice = Double.parseDouble(item.findElement(By.xpath("./td[@class=\"b-cell-1\"]/p[@class=\"price price-primary\"]/a/span")).getText());
            String foundHrefSeller = item.findElement(By.xpath("./td[@class=\"b-cell-1\"]/p[@class=\"price price-primary\"]/a")).getAttribute("href");
            Seller seller = new Seller();
            seller.refSeller = foundRefSeller;
            seller.price = foundPrice;
            seller.hrefSeller = foundHrefSeller;
            seller.product = this.product;
            listSellers.add(seller);
        }
        return listSellers;
    }

}

