import java.util.ArrayList;

public interface WebScraper {

    public void setKeyWords(String keyWords);

    /**
     * Gets a raw list of products from a website, takes the number of pages to be searched as a parameter
     * @param pages
     * @return
     */
    public ArrayList<Product> getRawProductList(int pages);


}
