package ftp;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FTPClient {

    private static final Logger logger = LogManager.getLogger(FTPClient.class);

    private Socket commandScoket;
    private BufferedReader commandReader;
    private BufferedWriter commandWriter;
    private int lastReplyCode;
    private String lastReplyMessage;
    private Socket dataSocket;
    private BufferedReader dataReader;
    private BufferedWriter dataWriter;


    public void connect(String server, int port) throws IOException {
        InetAddress ipAddress = InetAddress.getByName(server);
        commandScoket = new Socket(ipAddress, port);

        commandReader = new BufferedReader(new InputStreamReader(commandScoket.getInputStream()));
        commandWriter = new BufferedWriter(new OutputStreamWriter(commandScoket.getOutputStream()));

        readCommandResponse();
    }

    private int readCommandResponse() throws IOException {
        lastReplyMessage = commandReader.readLine();
        logger.debug("Response: " + lastReplyMessage);

        lastReplyCode = Integer.parseInt(lastReplyMessage.split(" ")[0]);
        return lastReplyCode;
    }

    public int getReplyCode() {
        return lastReplyCode;
    }

    public boolean login(String user, String pass) throws IOException {
        sendCommand("USER " + user);

        if (readCommandResponse() != 331) {
            return false;
        }
        sendCommand("PASS " + pass);
        if (readCommandResponse() != 230) {
            return false;
        }

        return true;
    }

    private boolean openDataChannel() throws IOException {
        //Implement only passive mode
        sendCommand("PASV");
        if (readCommandResponse() != 227) {
            return false;
        }
        int startPos = lastReplyMessage.indexOf('(');
        int endPos = lastReplyMessage.indexOf(')');
        String encodedAddres = lastReplyMessage.substring(startPos + 1, endPos);
        String[] numbers = encodedAddres.split(",");

        String serverAddress = numbers[0] + "." + numbers[1] + "." + numbers[2] + "." + numbers[3];
        int port = Integer.parseInt(numbers[4]) * 256 + Integer.parseInt(numbers[5]);

        dataSocket = new Socket(InetAddress.getByName(serverAddress), port);
        dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
        dataWriter = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));

        return true;
    }

    private void closeDataChannel() throws IOException {
        dataReader = null;
        dataWriter = null;
        dataSocket.close();
        dataSocket = null;
    }

    private void sendCommand(String command) throws IOException {
        logger.debug("Request: " + command);
        commandWriter.write(command);
        commandWriter.write("\r\n");
        commandWriter.flush();
    }

    public boolean isConnected() {
        return commandScoket != null;
    }

    public List<FTPFile> list() throws IOException {
        openDataChannel();
        sendCommand("LIST");
        if (readCommandResponse() != 150) {
            throw new IllegalStateException("Unexpected response");
        }

        List<FTPFile> files = parseFilesData();
        closeDataChannel();
        if (readCommandResponse() != 226) {
            throw new IllegalStateException("Unexpected response");
        }
        return files;
    }

    private List<FTPFile> parseFilesData() throws IOException {

        logger.debug("Reading files list...");
        List<FTPFile> result = new ArrayList<>();

        while (true) {
            String line = dataReader.readLine();
            if (line != null) {
                logger.debug("read line: " + line);
                result.add(parseFile(line));
            }
            else {
                break;
            }
        }

        logger.debug("All files read");

        return result;
    }

    private FTPFile parseFile(String line) {
        String[] tokens = line.split("\\s+");
        String name = tokens[8];
        String attrs = tokens[0];

        return new FTPFile(name, attrs);
    }

    public boolean changeWorkingDirectory(String name) throws IOException {
        sendCommand("CWD " + name);
        return  (readCommandResponse() == 250);
    }

    public boolean makeDirectory(String name) throws IOException {
        sendCommand("MKD " + name);
        return readCommandResponse() == 257;
    }

    public void disconnect() throws IOException {
        sendCommand("QUIT");
        readCommandResponse();

        commandWriter = null;
        commandReader = null;
        commandScoket.close();
        commandScoket = null;
    }
}
