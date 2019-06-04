package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

import java.io.*;

public class Task5 {

    @Test(description = "Task 5: two scripts", parameters = {"scriptParameter"}, enabled = true)
    public void test5(@Optional String scriptParameter) {

        final String scriptPath = "/src/Task5/";
        String script1 = "Task5_1";
        runScript(scriptPath + script1, "");
        String script2 = "Task5_1";
        runScript(scriptPath + script2, scriptParameter);

    }

    private void printStream(InputStream inputStream) throws IOException {
        String line;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    private void runScript(String script, String scriptParameter) {
        try {
            String curDir = System.getProperty("user.dir");
            Process process = Runtime.getRuntime().exec(curDir + script + " " + scriptParameter);

            printStream(process.getErrorStream());
            printStream(process.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
