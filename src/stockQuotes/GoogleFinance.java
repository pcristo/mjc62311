package stockQuotes;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Code to get stock information from Google Finance
 */
public class GoogleFinance implements StockQuotes{

    /**
     *
     * @param company company enum of stock to retrieve
     * @return last price
     * @throws IOException on retrieving URL
     * TODO pre-processing is ugly, needs to be done better or ideally not at all
     * TODO Convert price to intw
     */
    public String getStock(Company company) {
        try {
            URL google = new URL("http://www.google.com/finance/info?q=" + company.getExchangeCode() + "%3a" + company.getTicker());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(google.openStream()));

            String inputLine;
            String all = "";
            int i = 0;
            while ((inputLine = in.readLine()) != null) {
                // This is ugly! This happens because of google placing "//" on the second line..
                i++;
                if (i == 2) {
                    continue;
                }
                all += inputLine;
            }
            in.close();
            JSONObject obj = new JSONObject(all.replace("[", "").replace("]", ""));
            return obj.getString("l");
        } catch(IOException e) {
            // TODO
            return "";
        }
    }
}
