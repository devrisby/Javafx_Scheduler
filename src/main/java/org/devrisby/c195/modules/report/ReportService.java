package org.devrisby.c195.modules.report;

import org.devrisby.c195.modules.activity.LoginActivity;

import java.io.*;

/** Class for Report business logic */
public class ReportService {

    /** Record login attempt to file.
     *
     * Creates new file if it doesn't already exist
     * @param loginReport - LoginActivity instance detailing login attempt */
    public static void updateLoginActivity(LoginActivity loginReport){
        PrintWriter writer = null;
        try{
            File loginActivity = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "loginActivity.txt");

            if(!loginActivity.exists()) {
                loginActivity.createNewFile();
            }

            writer = new PrintWriter(new BufferedWriter(new FileWriter(loginActivity, true)));
            writer.println(loginReport.toString());
        } catch (IOException e) {
            System.out.println("Error writing login activity to file!\n" + e.getMessage());
            System.exit(1);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
