package tests;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import tutby.pages.FirstPage;
import tutby.pages.ResultPage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Task1 {

    private static final Logger logger = LogManager.getLogger(Task1.class);

    private WebDriver webDriver;

    @BeforeTest
    @Parameters({"browser"})
    public void setupTest(@Optional String browser) {
//        System.setProperty("webdriver.chrome.driver", "/Users/Olya/Applications/chromedriver");
//        System.setProperty("webdriver.gecko.driver", "/Users/Olya/Applications/geckodriver");

        if (browser != null && browser.equals("firefox")) {
            webDriver = new FirefoxDriver();
        } else {
            webDriver = new ChromeDriver();
        }
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    }


    @Test(description = "Task 1: to compare results of searching on tut.by with selenium and without")
    public void test1() throws Exception {
        logger.info("Task 1");
        String search2 = getTextSearch2();
        String search1 = getTextSearch1();
        if (search1.equals(search2)) {
            logger.info("Results with found first elements are the same. Result is: "+ search1 );
        }
        else
        {
            logger.info("Results with found first elements are different.\n First result is: " + search1 + "\n Second result is: " +search2);
        }

        // Steps for searching without selenium is realized in opentutby.js in current folder(you need to run 3 steps there on 3 pages consequently with console).
        //The results of searching is the same in both cases.
    }

    private String getTextSearch1() {
        /*
        1.осуществить переход (используя интерфейс страницы***):
        "Компьютеры и сети"->"Комплектующие"->"Видеокарты"
        */
        logger.info("1) Implementation with Selenium");
        webDriver.get("https://tut.by/");
        logger.info("Navigate to tut.by");

        FirstPage firstPage = new FirstPage(webDriver);
        firstPage.enterWordForSearch("лукашенко");

        ResultPage resultPage = firstPage.navigateToResultPage();
        String textSearch1 = resultPage.getNameOfFirstResult();
        logger.info("First result of searching with selenium: " + textSearch1);
        return textSearch1;
    }

    private String getTextSearch2() throws Exception {
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://tut.by");
            logger.debug(page.getTitleText());

            HtmlTextInput textField = (HtmlTextInput) page.getElementById("search_from_str");
            textField.type("лукашенко");

            HtmlSubmitInput button = page.getElementByName("search");
            HtmlPage page2 = button.click();
            HtmlElement el = (HtmlElement) page2.getByXPath("//li[@class =\"b-results__li m-market\"]/h3/a/b").get(0);
            String foundElement = el.getTextContent();
            return foundElement;

        }
    }

    @AfterMethod
    public void afterTest() {
        webDriver.quit();
    }

}
