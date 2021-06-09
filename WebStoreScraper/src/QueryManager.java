import java.util.ArrayList;

public class QueryManager {
    ArrayList<Product> productList;

    /**
     * The QueryManager can calculate the best deals, or the cheapest prices in an ArrayList of Products
     * @param productList
     */
    public QueryManager(ArrayList<Product> productList)
    {
        this.productList = productList;
    }


    private double convertPriceToDouble(Product product)
    {
        String productPriceString = product.getPrice().replace(",", "");
        //System.out.println("Product Price String: " + productPriceString);
        int toIndex = productPriceString.indexOf("$", 1);
        if(productPriceString.contains("e"))
            return -1;
        if(toIndex != -1)
            return Double.parseDouble(productPriceString.substring(1, toIndex).replace("US", ""));
        return Double.parseDouble(productPriceString.substring(1));
    }

    private void purifyPriceList()
    {
        for(int i = productList.size()-1; i >= 0; i--)
        {
            if(productList.get(i).getPrice().equals(""))
            {
                productList.remove(productList.get(i));
            }
            //if(productList.get(i).getDiscount().equals(""))
                //productList.get(i).setDiscount("$0.00 (0%)");
        }

    }

    private ArrayList<Product> purifyDiscountList()
    {
        ArrayList<Product> purifiedDiscountList = new ArrayList<Product>();
        for(int i = productList.size()-1; i >= 0; i--)
        {
            if(!(productList.get(i).getDiscount().equals("")))
            {
                purifiedDiscountList.add(productList.get(i));
            }
        }
        return purifiedDiscountList;
    }


    public Product findLowestPrice(String website)
    {
        purifyPriceList();

        if(website.equals("Best Buy"))
        {
            purifyBestBuyPrice();
            purifyBestBuyDiscount();
        }

        if(website.equals("EBay"))
        {
            purifyEBayDiscount();
            purifyEBayPrice();
        }

        double lowestPrice = convertPriceToDouble(productList.get(0));
        int j = 0;
        for(int i = 0; i < productList.size(); i++)
        {
            if((convertPriceToDouble(productList.get(i)) < lowestPrice) && (convertPriceToDouble(productList.get(i)) >= 0))
            {
                lowestPrice = convertPriceToDouble(productList.get(i));
                j = i;
            }
        }
        return productList.get(j);
    }

    public Product findBestDealDollars(String website)
    {
        //Format for the discountString - "0.00 (0%)"
        //"12.00 (4%)"
        if(website.equals("Best Buy"))
        {
            purifyBestBuyDiscount();
            purifyBestBuyPrice();
        }

        if(website.equals("EBay"))
        {
            purifyEBayDiscount();
            purifyEBayPrice();
        }


        ArrayList<Product> products = purifyDiscountList();
        //String discountString = products.get(0).getDiscount().substring(0, products.get(0).getDiscount().length()-5).replace("$", "").replace("(", "");

        double[] discounts = new double[products.size()];

        for(int i = 0; i < discounts.length; i++)
        {
            discounts[i] = Double.parseDouble(products.get(i).getDiscount().substring(0, products.get(i).getDiscount().length()-5).replace("$", "").replace("(", ""));
        }

        double bestDiscount = discounts[0];
        int j = 0;

        for(int i = 0; i < products.size(); i++)
        {
            if(discounts[i] > bestDiscount)
            {
                bestDiscount = discounts[i];
                j = i;
            }
        }
        return products.get(j);
    }

    public Product findBestDealPercentage(String website)
    {
        //$0.00 (0%) ----> "0.00 0"
        //$12.00 (24%) ----> "12.00 24"
        //$7.00 (24%) ----> "7.00 24"
        //$12.00 (4%) ----> "12.00 4"

        if(website.equals("Best Buy"))
        {
            purifyBestBuyDiscount();
            purifyBestBuyPrice();
        }

        if(website.equals("EBay"))
        {
            purifyEBayDiscount();
            purifyEBayPrice();
        }


        ArrayList<Product> products = purifyDiscountList();
        String pureString, discountString;

        double[] discounts = new double[products.size()];

        for(int i = 0; i < discounts.length; i++)
        {
            pureString = products.get(i).getDiscount().replace("$", "").replace("(", "").replace("%", "").replace(")", "");
            discountString = pureString.substring(pureString.length()-2, pureString.length()).replace(" ", "");
            discounts[i] = Double.parseDouble(discountString);
        }


        double bestDiscount = discounts[0];
        int j = 0;
        for(int i = 0; i < products.size(); i++)
        {
            if(discounts[i] > bestDiscount)
            {
                bestDiscount = discounts[i];
                j = i;
            }
        }
        return products.get(j);
    }

    private void purifyBestBuyPrice()
    {

        for(int i = 0; i < productList.size(); i++)
        {
            productList.get(i).setPrice("$" + productList.get(i).getPrice().substring(1, 9).replace("Y", "").replace("o", "").replace("u", "").replace("r", "").replace(" ", ""));
        }
    }






    private void purifyBestBuyDiscount()
    {
        double price, discountDollars;
        for(int i = 0; i < productList.size(); i++)
        {
            if(productList.get(i).getDiscount().equals(""))
                productList.get(i).setDiscount("$0 (0%)");
            else{
                price = Double.parseDouble(productList.get(i).getPrice().substring(1, 10).replace("Y", "").replace("o", "").replace("u", "").replace("r", "").replace(" ", ""));
                discountDollars = Double.parseDouble(productList.get(i).getDiscount().replace("Save $", ""));
                productList.get(i).setDiscount("$" + discountDollars + " (" + (int)((discountDollars/(price+discountDollars)) * 100) + "%)");
                //$12.00
            }
        }
    }


    private void purifyEBayDiscount()
    {
        //Was: US $18.12          <---- this is the formatting of the discount
        //what I want it to be ----> $18.12 (20%)

        purifyPriceList();

        double price, originalDollars, discountDollars;
        String tempString, tempString2, pureString;
        for(int i = 0; i < productList.size(); i++)
        {
            if(productList.get(i).getDiscount().equals(""))
                productList.get(i).setDiscount("$0 (0%)");
            else{
                //from 8 to the end is the original in dollars
                tempString = productList.get(i).getDiscount().replace("Was: ", "").replace("US $", "").replace("$", "");
                //System.out.println("TempString: " + tempString);
                tempString2 = productList.get(i).getPrice().replace(" ", "").replace("F", "").replace("R", "").replace("E", "").replace("$", "");

                System.out.println("TempString2: "+ tempString2);

                price = Double.parseDouble(tempString2);
                originalDollars = Double.parseDouble(tempString);
                discountDollars = Math.round((originalDollars-price)*100)/ (double) 100;

                //System.out.println("Discount Dollar String: " + discountDollars);

                //Discount: $42.0 (70%)

                pureString = "$" + discountDollars + " (" + (int)((discountDollars/(price+discountDollars)) * 100) + "%)";

                if(pureString.contains(".0 ("))
                    pureString = pureString.replace(".0", ".00");

                productList.get(i).setDiscount(pureString);
            }
        }
    }

    private void purifyEBayPrice()
    {
        //$18.00 FREE
        for(int i = 0; i < productList.size(); i++)
        {
            productList.get(i).setPrice(productList.get(i).getPrice().replace(" ", "").replace("F", "").replace("R", "").replace("E", ""));
        }

    }




    //make sure to remember that the best buy ones actually have their discounts in dollars, and not percents


    //this is the toString of a Best Buy Product:

//    HP - 27" IPS LED FHD FreeSync Monitor (2 x HDMI, VGA) - Silver and Black
//    https://www.bestbuy.com//site/hp-27-ips-led-fhd-freesync-monitor-2-x-hdmi-vga-silver-and-black/6454577.p?skuId=6454577
//    $189.99Your price for this item is $189.99
//    Save $70

}
