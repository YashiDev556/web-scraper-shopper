import java.util.ArrayList;
import java.util.Scanner;

public class ApplicationMain {
    public static void main (String[] args)
    {
        boolean isRunning = true;
        Scanner input = new Scanner(System.in);
        String performSearch;
        while(isRunning)
        {
            mainSearchProcess();
            System.out.println("Would you like to perform another search? (Y/n): ");
            performSearch = input.nextLine();
            if(performSearch.contains("n") || performSearch.contains("N") || performSearch.contains("q") || performSearch.contains("Q"))
                isRunning = false;

        }
    }

    public static void mainSearchProcess()
    {
        //setting up scanners and other variables
        ArrayList<Product> products = null;
        Scanner sc = new Scanner(System.in);
        Scanner sc2 = new Scanner(System.in);
        Scanner sc3 = new Scanner(System.in);
        String website = "";
        WebScraper webScraper;





        //asking for website
        System.out.println("Which Website do you want to search? \n 1.) Amazon  2.) Best Buy  3.) EBay \n Enter integer here: ");
        int a = sc.nextInt();

        if(a == 1)
            website = "Amazon";
        if(a == 2)
            website = "Best Buy";
        if(a == 3)
            website = "EBay";


        //ask for the product
        Scanner input = new Scanner(System.in);
        System.out.println("What Product would you like to search for? \n Enter search here: ");
        String keyWords = input.nextLine(); // getting a String value (full line)
        keyWords = keyWords.replace(" ", "+");
        //System.out.println(keyWords);

        //ask the pages to be searched
        System.out.println("How many pages do you want to be searched for the product? \n Enter Integer here: ");

        int pages = sc3.nextInt();


        //choose between cheapest product and other things
        System.out.println("Finally, what product in particular do you want to search for? \n 1.) Cheapest product " +
                " 2.) Best Deal in Dollars " +
                " 3.) Best Deal in Percentage \n Enter integer here: ");

        int whatToSearchFor = sc2.nextInt();

        if(website.equals("Amazon"))
        {
            webScraper = new AmazonWebScraper(keyWords);
            products = webScraper.getRawProductList(pages);
        }

        if(website.equals("Best Buy"))
        {
            webScraper = new BestBuyWebScraper(keyWords);
            products = webScraper.getRawProductList(pages);
        }

        if(website.equals("EBay"))
        {
            webScraper = new EBayWebscraper(keyWords);
            products = webScraper.getRawProductList(pages);
        }


        QueryManager queryManager = new QueryManager(products);



        System.out.println();
        System.out.println();


        if(whatToSearchFor == 1)
            System.out.println("Lowest Price Product: \n" + queryManager.findLowestPrice(website));

        if(whatToSearchFor == 2)
            System.out.println("Best Deal in Dollars: \n" + queryManager.findBestDealDollars(website));

        if(whatToSearchFor == 3)
            System.out.println("Best Deal in Percentage: \n" + queryManager.findBestDealPercentage(website));
    }
}
