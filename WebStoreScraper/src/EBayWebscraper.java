import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class EBayWebscraper implements WebScraper{

    private String keywords;

    public EBayWebscraper(String keywords)
    {
        this.keywords = keywords;

    }

    @Override
    public void setKeyWords(String keyWords) {

    }


    //class: s-item__link

    @Override
    public ArrayList<Product> getRawProductList(int pages) {
        ArrayList<Product> rawProductList = new ArrayList<Product>();

        System.out.println("Give the application a hot minute to show results. Grab a cup of hot chocolate while we " +
                "search EBay for you! :) \n");

        for (int i = 1; i <= pages; i++) {
            System.out.println();
            System.out.println("Searching Page: " + i);

            String url = "https://www.ebay.com/sch/i.html?_from=R40&_nkw=" + keywords + "&_sacat=0&LH_TitleDesc=0&_pgn=" + i;

            Document document = null;
            try {
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                System.out.println("Could not scan items :(");
                //return null;
            }

            assert document != null;
            Elements elements = document.getElementsByClass("s-item__link");
//            reconnect due to a bad gateway
            while (elements.size() == 0) {
                try {
                    document = Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                elements = document.getElementsByClass("s-item__link");
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

                String link = element.attributes().get("href");

                productTitle = "";
                productPrice = "";
                productDiscount = "";

                if(!element.attributes().get("href").equals(""))
                {

                    System.out.print(loadingAnim);

                    productURL = link;

                    try{
                        productPage = Jsoup.connect(productURL).get();
                    } catch (IOException e) {
                        ;
                    }
                    assert productPage != null;


                    if(productPage != null && productPage.getElementsByClass("notranslate").text().length() != 0)
                    {
                        productTitle = productPage.getElementsByClass("it-ttl").text();

                        productPrice = productPage.getElementsByClass("notranslate").text().substring(3, productPage.getElementsByClass("notranslate").text().length()).replace("/ea", "");

                        productDiscount = productPage.getElementsByClass("vi-originalPrice").text();
                    }

                    //Was: US $18.12          <---- this is the formatting of the discount
                    //what I want it to be ----> $18.12 (20%)

//                    System.out.println(productTitle);
//                    System.out.println(productURL);
//                    System.out.println(productPrice);
//                    System.out.println(productDiscount);

                    rawProductList.add(new Product(productTitle, productURL, productPrice, productDiscount));


                }
            }
        }
        return rawProductList;
    }

}
