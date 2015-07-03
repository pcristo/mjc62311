package install;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Compile {

    static String projectRoot = "C:\\Users\\Ross\\Dropbox\\Distributed_Systems\\project\\";



    public static void main(String args[]) {

        // Check if java file is provided
        String javac;
        try {
            javac = args[0];
        } catch(ArrayIndexOutOfBoundsException aioe) {
            javac = "javac";
        }

        String resources = "resources\\jar\\*";
        String output = "C:\\Users\\Ross\\Desktop\\testOutput";

        try {
            runProcess(javac + " -classpath "+ output + ";" + projectRoot + resources + " -d " + output +
                    " C:\\Users\\Ross\\Dropbox\\Distributed_Systems\\project\\src\\common\\util\\Config.java");


        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }



    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
    }

    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }
}
