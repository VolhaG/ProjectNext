package onliner.pages.offer;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import onliner.pages.BasePage;
import onliner.pages.SellerPage;
import onliner.tools.Product;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class OfferPage extends BasePage {
    public OfferPage(@Nonnull WebDriver driver) {
        super(driver);
    }

    public Product product;

    public SellerPage navigateToSeller(Seller seller) {
        logger.debug("Open sellers page: " + seller);

        performClick(seller.refSeller);
        SellerPage sellerPage = new SellerPage(webDriver);
        sellerPage.hrefName = seller.hrefSeller;
        return sellerPage;
    }

    public List<Seller> getSellers() {
        logger.debug("Collecting sellers...");
        List<Seller> listSellers = new ArrayList<>();
        List<WebElement> elements = webDriver.findElements(By.xpath("//tr[@class=\"state_add-to-cart m-divider\" or @class=\"marked state_add-to-cart m-divider\"]"));
        logger.debug("Found " + elements.size() + " sellers");

        for (WebElement item : elements) {
            WebElement foundRefSeller = item.findElement(By.xpath("./td[@class=\"b-cell-4\"]/div[@class=\"b-cell-4__line-1\"]/a[@class=\"logo\"]/img"));

            WebElement price = item.findElement(By.xpath("./td[@class=\"b-cell-1\"]/p[@class=\"price price-primary\"]/a/span"));
            String stringPrice = (price.getText()).replace(",", ".");
            Double foundPrice = Double.parseDouble(stringPrice);

            String foundHrefSeller = item.findElement(By.xpath("./td[@class=\"b-cell-1\"]/p[@class=\"price price-primary\"]/a")).getAttribute("href");
            Seller seller = new Seller(foundPrice, foundHrefSeller, foundRefSeller);
            listSellers.add(seller);
        }
        return listSellers;
    }

}

