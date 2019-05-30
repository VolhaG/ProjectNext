package onliner.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.annotation.Nonnull;

public class MainPage extends BasePage {

    @FindBy(xpath = "//div[@class=\"catalog-navigation\"]/ul/li[@data-id =\"2\"]")
    WebElement menuComputerAndNet;

    @FindBy(xpath = "//*[contains(text(),'Комплектующие')]")
    WebElement menuSets;

    @FindBy(xpath = "//a[@href=\"https://catalog.onliner.by/videocard\"]/span")
    WebElement unitVideoCard;

    public MainPage(@Nonnull WebDriver driver) {
        super(driver);
    }

    public VideoCardPage navigateToUnitVideoCard() {
        menuComputerAndNet.click();
        menuSets.click();
        unitVideoCard.click();
        return new VideoCardPage(webDriver);
    }
}
