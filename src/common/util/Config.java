package common.util;

import org.json.JSONObject;

import java.io.*;

/**
 * Class reads in from config.json in common.util package
 * Is used to get any configuration items set in this json file
 */
public class Config {

    private static class Holder{
        static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return Holder.INSTANCE;
    }

    private JSONObject configJson;

    public Config() {
        // Do not create, call getInstance
        try {
            setJson();
        } catch(Exception ioe){
            System.out.println("IOE ERROR: " + ioe.getMessage());
        }
    }



    /**
     * Gets the json file creates a json object for reference
     * @throws IOException
     */
    private void setJson() throws Exception {
        configJson = null;

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.json");

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }

        String configString = out.toString();   //Prints the string content read from input stream
        reader.close();
        configJson = new JSONObject(configString);
    }


    /**
     *
     * @param attr String name of item in config.json we are trying to retreive
     * @param raw boolean if we should put slashes to *nix style
     * @return String of json object or null if attribute doesn't exist.
     */
    public String getAttr(String attr, boolean raw) {
        try {
            String attribute = configJson.getString(attr);
            if(raw) {
                return attribute;
            } else {
            	if (attribute.startsWith("http"))
            		return attribute;
                return attribute.replace("/", File.separator);
            }
        } catch(org.json.JSONException joe) {
            System.out.println("Json Exception in config: attr = " + attr);
            System.out.println(joe.getMessage());
            return null;
        }
    }

    /**
     *
     * @param attr attribute wishing to obtain
     * @return
     */
    public String getAttr(String attr) {
        return getAttr(attr, false);
    }

    public String loadMacSecurityPolicy() {
        return this.configJson.getString("macLogServerHome") + configJson.getString("macSecurity");
    }




}
