package org.devrisby.c195.services;

import org.devrisby.c195.models.LoginActivity;

import java.io.*;

public class ReportServices {
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
            writer.close();
        }
    }
}
