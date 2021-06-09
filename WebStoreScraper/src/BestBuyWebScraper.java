import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class BestBuyWebScraper implements WebScraper {

    private String keyWords;

    /**
     * A webscraper that can extract product information from Best Buy's website
     * @param keyWords
     */
    public BestBuyWebScraper(String keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }


    @Override
    public ArrayList<Product> getRawProductList(int pages) {

        ArrayList<Product> rawProductList = new ArrayList<Product>();

        System.out.println("Give the application a hot minute to show results. Grab a cup of tea while we " +
                "search Best Buy for you! :) \n");

        for (int i = 1; i <= pages; i++) {
            System.out.println();
            System.out.println("Searching Page: " + i);

            String url = "https://www.bestbuy.com/site/searchpage.jsp?cp=" + i + "&id=pcat17071&st=" + keyWords;


            Document document = null;
            try {
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                System.out.println("Could not scan items :(");
                //return null;
            }

            assert document != null;
            Elements elements = document.getElementsByClass("image-link");
//            reconnect due to a bad gateway
            while (elements.size() == 0) {
                try {
                    document = Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                elements = document.getElementsByClass("image-link");
            }




            String productURL;
            String productTitle;
            String productPrice;
            String productDiscount;
            Document productPage = null;

            int x = 1;

            int numberOfElementsSearched = 0;

            String loadingAnim = ".";

            System.out.println("Finding products");

            //System.out.println("Elements: " + elements.size());



            for(Element element : elements)
            {
                String link = element.attributes().get("href");

                productTitle = "";
                productPrice = "";
                productDiscount = "";
                if(!element.attributes().get("href").equals("") && !element.attributes().get("href").contains("search"))
                {

                    System.out.print(loadingAnim);

                    productURL = "https://www.bestbuy.com/" + link;

                    try{
                        productPage = Jsoup.connect(productURL).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert productPage != null;

                    productTitle = productPage.getElementsByClass("heading-5 v-fw-regular").text();

                    productPrice = productPage.getElementsByClass("priceView-hero-price priceView-customer-price").text();

                    productDiscount = productPage.getElementsByClass("pricing-price__savings").text();

                    rawProductList.add(new Product(productTitle, productURL, productPrice, productDiscount));
                }
            }

        }
        return rawProductList;
    }
}
