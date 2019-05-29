package pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import javax.annotation.Nonnull;

public abstract class BasePage {

    protected final WebDriver webDriver;

    protected BasePage(@Nonnull WebDriver driver) {
        webDriver = driver;
        PageFactory.initElements(webDriver, this);
    }

}