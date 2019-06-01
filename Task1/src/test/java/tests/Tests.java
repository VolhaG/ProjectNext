package tests;

import onliner.pages.MainPage;
import onliner.pages.SellerPage;
import onliner.pages.VideoCardPage;
import onliner.pages.offer.OfferPage;
import onliner.pages.offer.Seller;
import onliner.tools.OfferPageHelper;
import onliner.tools.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import tutby.pages.FirstPage;
import tutby.pages.ResultPage;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.text.html.HTML;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.nodes.Document;


//import static sun.corba.Bridge.get;
import org.jsoup.nodes.Element;

public class Tests {

    private static final Logger logger = LogManager.getLogger(Tests.class);

    private WebDriver webDriver;

    @BeforeTest
    @Parameters({"browser"})
    public void setupTest(@Optional String browser) {
        System.setProperty("webdriver.chrome.driver", "/Users/Olya/Applications/chromedriver");
        System.setProperty("webdriver.gecko.driver", "/Users/Olya/Applications/geckodriver");

        if (browser != null && browser.equals("firefox")) {
            webDriver = new FirefoxDriver();
        } else {
            webDriver = new ChromeDriver();
        }
//        webDriver = new FirefoxDriver();
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    }

    @Test(description = "Task 3: test case for onliner", priority = 3 , parameters = {"rating"}, enabled = false)
    public void test3(String rating) throws InterruptedException {
        /*
        1.осуществить переход (используя интерфейс страницы***):
        "Компьютеры и сети"->"Комплектующие"->"Видеокарты"
        */
        logger.info("Task 3");
        webDriver.get("https://catalog.onliner.by/");
        logger.info("Navigate to catalog.onliner.by");

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

        Thread.sleep(5_000);

        /*
        3. из предложенных товаров на первой странице (вид упорядочения: Сначала популярные)
        выбрать самый недорогой вариант С РЕЙТИНГОМ по отзывам "4.5" и более.
        Вывести название продукта в лог.
                Кликнуть на кнопку выбора предоложений "{N} Предложений"
                *Если нет подходящих товаров, просто выбрать самый дешевый
                *Если подходящих товаров несколько, выбрать любой.
        */
        List<Product> products = videoCardPage.getProducts();
        if (products.size() == 0) {
            logger.info("No such products.");
            return;
        }
        List<Product> productsWithRating = videoCardPage.productsSelectedByRating(products,rating);
        if (productsWithRating.size() == 0) {
            logger.info("No products with rating 4.5 stars. Products will be selected without rating.");
            productsWithRating = products;
        }
        Product product = videoCardPage.findProductWithCheapestPrice(productsWithRating);

        logger.info("Popular product name with rating upper 4.5 stars and the cheapest price "+ Double.toString(product.price) + " p.: "+ product.name);


        /*4. На странице предложений найти продавца с наименьшей ценой за товар.
        *Если подходящих продавцов несколько, выбрать любого
        Кликнуть на ссылку с логотипом продавца.
        */
        OfferPage offerPage = videoCardPage.navigateToOfferPageOfProduct(product);
        if (offerPage == null) {
            logger.info("No suggestions for the product!");
            return;
        }
        List<Seller> sellers = offerPage.getSellers();
        Seller seller = OfferPageHelper.selectSellerWithCheapestPrice(sellers);
        SellerPage sellerPage = offerPage.navigateToSeller(seller);

        /*
        5. на открывшейся странице найти название продавца и вывести в лог
        (пример: "https://6498.shop.onliner.by/" : "Ньютон")
        */

        if (!(sellerPage == null)) {
            logger.info(sellerPage.hrefName + " : "+ sellerPage.getTitleName());
        }
        else{
            logger.info("No seller for the product.");
        }
    }

    @Test(description = "Task 1: to compare result of searching on tut.by with selenium and without", priority = 1, enabled = false)
    public void test1() {
       getTextSearch1();
    }

    @Test(description = "Task 5: two scripts", priority = 5, parameters = {"scriptParameter"}, enabled = true)
    public void test5(@Optional String sriptParameter)  {

        final String scriptPath = "./src/Task5/";

        String script1 = "Task5_1.sh";
        runScript(scriptPath + script1);
        String script2 = "Task5_1.sh";
        runScript(scriptPath + script2);

    }

    @AfterMethod
    public void afterTest() {
        webDriver.quit();
    }


    public String getTextSearch1() {
        /*
        1.осуществить переход (используя интерфейс страницы***):
        "Компьютеры и сети"->"Комплектующие"->"Видеокарты"
        */
        logger.info("Task 1");
        webDriver.get("https://tut.by/");
        logger.info("Navigate to tut.by");

        FirstPage firstPage = new FirstPage(webDriver);
        firstPage.enterWordForSearch("лукашенко");

        ResultPage resultPage = firstPage.navigateToResultPage();
        String textSearch1 = resultPage.getNameOfFirstResult();
        logger.info("First result of searching with selenium: "+ textSearch1);
        return textSearch1;

    }

    private void runScript(String script) {
        try {
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", script, "","");
            Process p = pb.start();
            p.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
