package onliner.tools;

import org.openqa.selenium.WebElement;

public class Product {
    public String name;
    public String rating;
    public Double price;
    public WebElement offers;
    public Product(String name, String rating, Double price, WebElement offers) {
        super();
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.offers = offers;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", rating='" + rating + '\'' +
                ", price=" + price +
                '}';
    }
}
