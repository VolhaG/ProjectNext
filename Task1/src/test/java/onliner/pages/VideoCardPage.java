package onliner.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import onliner.pages.offer.OfferPage;
import onliner.tools.Product;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

public class VideoCardPage extends BasePage {

    @FindBy(xpath = "//*[@id=\"schema-filter\"]/div[2]/div[7]/div[3]/div/div[1]/input")
    WebElement startDate;

    @FindBy(xpath = "//*[@id=\"schema-filter\"]/div[2]/div[7]/div[3]/div/div[2]/input")
    WebElement endDate;

    @FindBy(xpath = "//*[@id=\"schema-filter\"]/div[2]/div[10]/div[3]/div/div[1]/select")
    WebElement sizeVC1;

    @FindBy(xpath = "//*[@id=\"schema-filter\"]/div[2]/div[10]/div[3]/div/div[2]/select")
    WebElement sizeVC2;

    @FindBy(xpath = "//ul/li/label/span[contains(text(),'GDDR5')]")
    WebElement typeOfVC;

    WebElement buttonOfferPage;

    WebElement raitingUpper45;

    @FindBy(xpath = "//div[class=\"schema-product\" ]")
    WebElement products;

    @FindBy(xpath = "//*[@id=\"schema-filter\"]/div[2]/div[12]/div[2]/span")
    WebElement elementToSroll;

    protected VideoCardPage(@Nonnull WebDriver driver) {
        super(driver);
    }

    public void setFilterByDates(String data1, String data2) {
        startDate.sendKeys(data1);
        endDate.sendKeys(data2);
    }

    public void setFilterBySizeVC(String size1, String size2) {
        sizeVC1.sendKeys(size1);
        sizeVC2.sendKeys(size2);
    }

    public void setFilterByTypeOfVC() {
        Actions actions = new Actions(webDriver);
        actions.moveToElement(elementToSroll);
        actions.perform();
        sleep(1_000);
        typeOfVC.click();
    }

    public OfferPage navigateToOfferPageOfProduct(Product product) {
        logger.debug("Navigate to: " + product);

        performClick(product.offers);

        OfferPage offerPage = new OfferPage(webDriver);
        offerPage.product = product;
        return offerPage;
    }

    public String findProductNameWithCheapestPrice(List<Product> products) {
        Double minPrice = Double.MAX_VALUE;
        String name = "";
        for (Product element : products) {
            if (element.price < minPrice) {
                minPrice = element.price;
                name = element.name;
            }
        }
        return name;
    }

    public Product findProductWithCheapestPrice(List<Product> products) {
        Double minPrice = Double.MAX_VALUE;
        Product product = null;
        for (Product element : products) {
            if (element.price < minPrice) {
                minPrice = element.price;
                product = element;
            }
        }
        return product;
    }


    public List<Product> getProducts() {
        logger.debug("Collecting products...");

        List<Product> products = new ArrayList<>();
        List<WebElement> elements = webDriver.findElements(By.xpath("//div[@class=\"schema-product__group\"]"));
        logger.debug("Found " + elements.size() + " products");

        for (WebElement item : elements) {
            String nameProduct = item.findElement(By.xpath(".//div[@class=\"schema-product__title\"]/a/span")).getText();
            logger.debug("Name: " + nameProduct);
            Double priceProduct = detectPrice(item.findElement(By.xpath(".//div[@class=\"schema-product__price\"]/a/span")).getText());
            String ratingProduct = getProductRating(item);
            WebElement offersProduct = item.findElement(By.xpath(".//div[@class=\"schema-product__offers\"]/a"));

            Product product = new Product(nameProduct, ratingProduct, priceProduct, offersProduct);
            products.add(product);
            logger.debug("New product added: " + product);
        }

        return products;
    }

    private String getProductRating(WebElement productElem) {
        try {
            WebElement ratingElement = productElem.findElement(By.xpath(".//a[@class=\"schema-product__rating\"]/span"));
            return detectRating(ratingElement.getAttribute("class"));
        } catch (NoSuchElementException exc) {
            logger.debug("No rating found");
            return "";
        }
    }

    private Double detectPrice(String text) {
        text = text.replace(" р.", "");
        text = text.replace(",", ".");
        return parseDouble(text);
    }

    private String detectRating(String text) {
        Integer symbol1 = text.length() - 2;
        String rating = text.substring(symbol1);
        return rating;
    }

    public List<Product> productsSelectedByRating(List<Product> products, String s) {
        List<Product> newProducts = new ArrayList<Product>();
        for (Product item : products) {
            if (item.rating.isEmpty()) {
               continue;
            }
            if (Integer.parseInt(item.rating) >= Integer.parseInt(s)) {
                newProducts.add(item);
                logger.debug("Product selected by rating: " + item.name);
            }
        }

        return newProducts;
    }

}
