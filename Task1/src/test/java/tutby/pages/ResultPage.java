package tutby.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import tutby.InitPage;

import javax.annotation.Nonnull;

public class ResultPage extends InitPage {

    protected ResultPage(@Nonnull WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//div[@class=\"search-i\"]/*//a/b")
    WebElement firstresult;

    public String getNameOfFirstResult() {
        return firstresult.getText();
    }

}
