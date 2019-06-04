package tests;

import ftp.FTPClient;
import ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.annotations.*;

import java.io.*;

import java.util.List;


public class Task4 {

    private static final Logger logger = LogManager.getLogger(Task1.class);

    @Test(description = "Task 4: creation ftp client")
    public void test4() throws IOException {

        logger.info("Task 4");
        String server = "ftp.byfly.by";
        // String server = "192.168.100.2";
        int port = 21;
        String user = "anonymous";
        String pass = "anonymous";

        FTPClient ftp = new FTPClient();

        ftp.connect(server, port);
        int replyCode = ftp.getReplyCode();
        if (replyCode != 220) {
            System.out.println("Operation failed.");
            return;
        }
        boolean success = ftp.login(user, pass);
        if (!success) {
            System.out.println("Failed to log into the server.");
            return;
        } else {
            System.out.println("Logged in server.");
            logger.info("Logged in server.");
        }

        if (ftp.isConnected()) {
            List<FTPFile> directories = ftp.list();
            for (FTPFile ftpfile : directories) {
                if (ftpfile.isDirectory()) {
                    if (ftp.changeWorkingDirectory(ftpfile.getName())) {
                        logger.info("We have enter to " + ftpfile.getName());
                        ftp.changeWorkingDirectory("..");
                    } else {
                        logger.info("Failed to enter to " + ftpfile.getName());
                    }
                }

            }
            if (ftp.makeDirectory("NewDir")) {
                logger.info("Directory was successfully created.");
            } else {
                logger.info("Directory wasn't created.");
            }

        }
        ftp.disconnect();
    }

}
