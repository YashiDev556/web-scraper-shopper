

public class Product {
    //fields
    public String name, url;
    public String price, discount;

    /**
     * An object that represents a product
     * @param name
     * @param url
     * @param price
     * @param discount
     */
    //constructor
    public Product(String name, String url, String price, String discount) {
        this.name = name;
        this.url = url;
        this.price = price.replace(",", "");
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    @Override
    public String toString()
    {
        return "Name: " + name + "\n" + "Product URL: " + url + "\n" + "Price: " + price + "\n" + "Discount: " + discount;
    }
}
