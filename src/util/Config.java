package util;

import org.json.JSONObject;

import java.io.*;

/**
 * Class reads in from config.json in util package
 * Is used to get any configuration items set in this json file
 */
public class Config {

    private static class Holder{
        static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {

        Config c = Holder.INSTANCE;
        try {
            c.setJson();
        } catch(Exception ioe){
            System.out.println("IOE ERROR: " + ioe.getMessage());
        }
        return c;
    }

    // TODO validate json

    //private static volatile Config instance = null;


    private JSONObject configJson;

//    public Config() {
//        // Do not create, call getInstance
//    }

    /**
     * Only one object per application
     * No setters, so does not need to be thread safe
     * Creates and aprses json object
     * @return Config object
     */
//    public static Config getInstance() {
//
//
//        if(instance == null) {
//            instance = new Config();
//        }
//        try {
//            instance.setJson();
//        } catch(Exception ioe){
//            System.out.println("IOE ERROR: " + ioe.getMessage());
//        }
//        return instance;
//    }

    /**
     * Gets the json file creates a json object for reference
     * @throws IOException
     */
    private void setJson() throws Exception {
        configJson = null;

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.json");

      // File f = new File("C:\\Users\\Ross\\Dropbox\\Distributed_Systems\\project\\src\\config.json");
      //  InputStream in = new FileInputStream(f);

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
     * @return String of json object or null if attribute doesn't exist.
     */
    public String getAttr(String attr) {
        try {
            String attribute = configJson.getString(attr);
            return attribute.replace("/", File.separator);
        } catch(org.json.JSONException joe) {
            System.out.println("Json Exception in config");
            return null;
        }
    }

    /**
     * Get security policy for RMI communication
     * @return String of location to security policy
     */
    public String loadSecurityPolicy() {

        // TODO untested
        // TODO fix this for realsies
        if( System.getProperty("os.name").toLowerCase().contains("mac")){
            return loadMacSecurityPolicy();
        } else {
            return this.configJson.getString("projectHome") + configJson.getString("security");
        }
    }

    public String loadMacSecurityPolicy() {
        return this.configJson.getString("macLogServerHome") + configJson.getString("macSecurity");
    }




}
