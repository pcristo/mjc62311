package WebServices;

import common.logger.LoggerClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Class wrapping Rest client calls
 */
public class Rest {

    /**
     * Make a post call and get response
     * @param url String or service
     * @param params Map of parameters to pass alon
     * @return String of data returned by service | null if connection failed
     */
    public static String getPost(String url, HashMap<String, String> params) {
        // Make RESTfull Call
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
//			con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");


            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            // Build parameters
            String urlParameters = "";
            int i = 1;
            for(Map.Entry<String, String> entry: params.entrySet()) {
                urlParameters += entry.getKey() + "=" + entry.getValue();
                if( i != params.size()) {
                    urlParameters += "&";
                } else {
                    i += 1;
                }
            }
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // Get response
            int responseCode = con.getResponseCode();
            if(responseCode != 200) {
                return null;
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();

            try {
                JSONObject res = new JSONObject(response.toString());
                boolean success = res.getBoolean("success");
                if(success) {
                    // Return string data
                    return res.getString("data");
                } else {
                    return null;
                }
            } catch(Exception err) {
                LoggerClient.log("ERROR reading JSON response in REST");
                return null;
            }
        } catch (Exception e) {
            LoggerClient.log("ERROR CALLING BROKER WS in REST");
            return null;
        }

    }
}
