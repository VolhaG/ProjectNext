package tests;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import pages.MainPage;
import pages.offer.OfferPage;
import pages.SellerPage;
import pages.VideoCardPage;
import tools.OfferPageHelper;
import tools.Product;
import pages.offer.Seller;

import java.util.List;

public class OnlinerTests {

    private static final Logger logger = LogManager.getLogger(OnlinerTests.class);

    private WebDriver webDriver;

    @BeforeMethod
    public void setupTest() {
        System.setProperty("webdriver.chrome.driver", "/Users/Olya/Applications/chromedriver");

        webDriver = new ChromeDriver();

        webDriver.navigate().to("https://catalog.onliner.by/");
        logger.info("Navigate to catalog.onliner.by");
    }

    @Test
    public void test1() {
        /*
        1.осуществить переход (используя интерфейс страницы***):
        "Компьютеры и сети"->"Комплектующие"->"Видеокарты"
        */
        MainPage mainPage = new MainPage(webDriver);
        VideoCardPage videoCardPage = mainPage.navigateToUnitVideoCard();

        /*
        2. Настроить фильтры поиска(используя интерфейс страницы***):
        "Дата выхода на рынок" : НЕ старше 4 лет.
        "Видеопамять" : 4Гб-8Гб
        "Тип видеопамяти" : "GDDR5"
        */
        videoCardPage.setFilterByDates("2016", "2019");
        videoCardPage.setFilterBySizeVC("4 Гб", "8 Гб");
        videoCardPage.setFilterByTypeOfVC();

        /*
        3. из предложенных товаров на первой странице (вид упорядочения: Сначала популярные)
        выбрать самый недорогой вариант С РЕЙТИНГОМ по отзывам "4.5" и более.
        Вывести название продукта в лог.
                Кликнуть на кнопку выбора предоложений "{N} Предложений"
                *Если нет подходящих товаров, просто выбрать самый дешевый
                *Если подходящих товаров несколько, выбрать любой.
        */
        List<Product> products = videoCardPage.initListWithProducts();
        if (products.size() == 0) {
            logger.info("No such products.");
            return;
        }
        List<Product> productsWithRating = videoCardPage.productsListSelectedByRating(products,"45");
        if (productsWithRating.size() == 0) {
            logger.info("No products with rating 4.5 stars. Products will be selected without rating.");
            productsWithRating = products;
        }
        String productName = videoCardPage.findProductNameWithCheapestPrice(productsWithRating);

        logger.info("Popular product with rating upper 4.5 stars and the cheapest price:"+ productName);


        /*4. На странице предложений найти продавца с наименьшей ценой за товар.
        *Если подходящих продавцов несколько, выбрать любого
        Кликнуть на ссылку с логотипом продавца.
        */
        OfferPage offerPage = videoCardPage.navigateToOfferPage();
        List<Seller> sellers = offerPage.getSellers();
        Seller seller = OfferPageHelper.selectSellerWithCheapestPrice(sellers);
        SellerPage sellerPage = offerPage.navigateToSeller(seller);

        /*
        5. на открывшейся странице найти название продавца и вывести в лог
        (пример: "https://6498.shop.onliner.by/" : "Ньютон")
        */
        logger.info(sellerPage.refName + " : "+ sellerPage.getTitleName());
    }

    @AfterTest
    public void afterTest() {
        webDriver.quit();
    }

}
