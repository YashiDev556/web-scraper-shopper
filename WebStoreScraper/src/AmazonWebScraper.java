import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class AmazonWebScraper implements WebScraper{
    //fields

    private String keyWords;

    /**
     * A webscraper that can extract product information from Amazon
     * @param keyWords
     */
    //constructor
    public AmazonWebScraper(String keyWords)
    {
        this.keyWords = keyWords;
    }

    //methods

    @Override
    public void setKeyWords(String keyWords)
    {
        this.keyWords = keyWords;
    }

    @Override
    public ArrayList<Product> getRawProductList(int pages)  //will return ArrayList<Product>
    {
        ArrayList<Product> rawProductList = new ArrayList<Product>();

        System.out.println("Give the application a hot minute to show results. Grab a cup of coffee while we " +
                "search Amazon for you! :) \n");
        for(int i = 1; i <= pages; i++)
        {
            System.out.println();
            System.out.println("Searching Page: " + i);
            //https://www.amazon.com/s?k=%22+keyWords+%22&page=3&qid=1622055189&ref=sr_pg_3
            String url = "https://www.amazon.com/s?k=" + keyWords + "&qid=1622055189&ref=sr_pg_" + i;



            //System.out.println(url);

            //System.out.println("Main Product List Link: " + url);

            Document document;
            try{
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                System.out.println("Could not scan items :(");
                return null;
            }

            Elements elements = document.getElementsByClass("a-link-normal a-text-normal");
            //reconnect due to a bad gateway
            while(elements.size() == 0)
            {
                try {
                    document = Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                elements = document.getElementsByClass("a-link-normal a-text-normal");
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
            for(Element element : elements)
            {

                System.out.print(loadingAnim);


                String link = element.attributes().get("href");
                productTitle = "";
                productPrice = "";
                productDiscount = "0";
                if(!link.contains("adsystem"))
                {
                    productURL = "https://www.amazon.com/" + link;
                    //System.out.println(link);
                    try{
                        productPage = Jsoup.connect(productURL).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert productPage != null;


                    //reconnect due to a bad gateway
                    while(productTitle.equals(""))
                    {
                        try{
                            productPage = Jsoup.connect(productURL).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        assert productPage != null;
                        productTitle = productPage.getElementsByClass("a-size-large product-title-word-break").text();
                    }


                    productPrice = productPage.getElementsByClass("a-size-medium a-color-price priceBlockBuyingPriceString").text();


                    productDiscount = productPage.getElementsByClass("a-span12 a-color-price a-size-base priceBlockSavingsString").text();


                    rawProductList.add(new Product(productTitle, productURL, productPrice, productDiscount));

                    numberOfElementsSearched++;

                }
            }
        }
        return rawProductList;
    }



}
