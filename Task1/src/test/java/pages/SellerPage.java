package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.annotation.Nonnull;

public class SellerPage extends BasePage{

    public String refName;

    public SellerPage(@Nonnull WebDriver driver) {
        super(driver);
    }
    @FindBy(xpath = "/div[@class=\"header-sells shop-intro\"]/h1")
    WebElement title;

    public String getTitleName() {
        return title.getText();
    }
}
