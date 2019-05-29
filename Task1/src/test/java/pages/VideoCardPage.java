package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import pages.offer.OfferPage;
import tools.Product;

import javax.annotation.Nonnull;
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
        typeOfVC.click();
    }

    public OfferPage navigateToOfferPage() {
        initButtonOfferPage();
        buttonOfferPage.click();
        Product product = initProduct();
        OfferPage offerPage = new OfferPage(webDriver);
        offerPage.product = product;
        return offerPage;
    }

    private Product initProduct() {
        return new Product();
    }

    public void initButtonOfferPage() {

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

    public List<Product> initListWithProducts() {
        List<Product> products = new ArrayList<Product>();
        String nameProduct;
        String ratingProduct;
        Double priceProduct;
        WebElement offersProduct;

        List<WebElement> elements = webDriver.findElements(By.xpath("//div[@class=\"schema-product__group\"]"));
        for (WebElement item : elements) {
            nameProduct = "";
            ratingProduct = "";
            priceProduct = 0.0;
            offersProduct = null;

            Product product = new Product();
            nameProduct = item.findElement(By.xpath(".//*/div[@class=\"schema-product__part schema-product__part_4\"]/div[@class=\"schema-product__title\"]/a/span")).getText();
            try {
                WebElement ratingElement = item.findElement(By.xpath(".//*/div[@class=\"schema-product__part schema-product__part_4\"]/div[@class=\"schema-product__info\"]/div[@class=\"schema-product__rating-group\"]/a[@class=\"schema-product__rating\"]/span"));
                ratingProduct = detectRating(ratingElement.getAttribute("class"));
            } catch (Exception e) {
            }

            try {
            priceProduct = detectPrice(item.findElement(By.xpath(".//*/div[@class=\"schema-product__part schema-product__part_3\"]/div/div/div[@class=\"schema-product__price\"]/a/span")).getText());
            }
            catch (Exception e){
            }

            try {
                offersProduct = item.findElement(By.xpath(".//*/div[@class=\"schema-product__part schema-product__part_3\"]/div/div/div[@class=\"schema-product__offers\"]/a"));
            }
            catch (Exception e) {
            }

            product.name = nameProduct;
            product.rating = ratingProduct;
            product.price = priceProduct;
            product.offers = offersProduct;
            products.add(product);
        }

        return products;
    }

    private Double detectPrice(String text) {
        text = text.replace(" р.","");
        text = text.replace(",",".");
        return parseDouble(text);
    }

    private String detectRating(String text) {
        Integer symbol1 = text.length() - 2;
        String rating = text.substring(symbol1);
        return rating;
    }

    public List<Product> productsListSelectedByRating(List<Product> products, String s) {
        List<Product> newProducts = new ArrayList<Product>();

        for (Product item : products) {
            if (item.rating.equals("s")) {
                newProducts.add(item);
            }
        }

        return newProducts;
    }

}
