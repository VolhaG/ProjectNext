package onliner.tools;

import onliner.pages.offer.Seller;

import java.util.List;

public abstract class OfferPageHelper {
    public static Seller selectSellerWithCheapestPrice(List<Seller> sellers) {
        Seller seller = null;
        Double minPrice = Double.MAX_VALUE;
        for (Seller item: sellers) {
            if (item.price < minPrice){
                minPrice = item.price;
                seller = item;
            }
        }

        return seller;
    }

}
