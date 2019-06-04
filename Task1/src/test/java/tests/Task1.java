package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import tutby.pages.FirstPage;
import tutby.pages.ResultPage;

import java.util.concurrent.TimeUnit;

public class Task1 {

    private static final Logger logger = LogManager.getLogger(Task1.class);

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


    @Test(description = "Task 1: to compare result of searching on tut.by with selenium and without", priority = 1)
    public void test1() {
        getTextSearch1();
        // Steps for searching without selenium is realized in opentutby.js in current folder(you need to run 3 steps there on 3 pages consequently with console).
        //The results of searching is the same in both cases.
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
        logger.info("First result of searching with selenium: " + textSearch1);
        return textSearch1;

    }

}
