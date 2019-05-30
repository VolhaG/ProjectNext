package tutby;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import javax.annotation.Nonnull;

public abstract class BasePage {

    protected final WebDriver webDriver;
    protected final Logger logger;

    protected BasePage(@Nonnull WebDriver driver) {
        webDriver = driver;
        logger = LogManager.getLogger(this.getClass());
        PageFactory.initElements(webDriver, this);
        logger.debug("Page initialized");
    }

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void performClick(WebElement webElement) {
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webElement)
                .perform();

        sleep(1_000);
        webElement.click();
    }


}