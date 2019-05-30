package tutby.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import tutby.InitPage;

import javax.annotation.Nonnull;

public class FirstPage extends InitPage {

    @FindBy(xpath = "//input[@id=\"search_from_str\"]")
    WebElement fieldSearch;

    @FindBy(xpath = "//input[@name=\"search\" and @type = \"submit\"]")
    WebElement search;

    public FirstPage(@Nonnull WebDriver driver) {
        super(driver);
    }

    public void enterWordForSearch(String text) {
        fieldSearch.sendKeys(text);
    }

    public ResultPage navigateToResultPage() {
        search.click();
        return new ResultPage(webDriver);
    }

}

