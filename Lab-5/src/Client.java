
import java.net.*;
import java.io.*;
public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {

        String UL;

        String times;

        String RY;

        String URL;

        String urlData = "";

        String modifiedSentence;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket("localhost", 6789);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        URL = inFromServer.readLine();

        System.out.println("Received: "+ URL);

        BufferedInputStream in = new BufferedInputStream(new URL(URL).openStream());

        for(int i = in.available();i>0;i--)

            urlData = urlData + (char)in.read();

        in.close();

        System.out.println("Data: "+urlData);

        System.out.println("Enter Choices: U/L How many R/Y");

        UL = inFromUser.readLine();

        times = inFromUser.readLine();

        RY = inFromUser.readLine();

        outToServer.writeBytes(urlData.length()+" "+UL+" "+times+" "+RY + '\n');

        char[] holder = urlData.toCharArray();

        for(int i=0; i<urlData.length();i++){

            outToServer.writeChar(holder[i]);

        }

        System.out.println("ORIGINAL: "+ urlData);

        System.out.println("After Case Change");

        modifiedSentence = inFromServer.readLine();

        System.out.println(modifiedSentence);

        System.out.println("After Shift Change");

        modifiedSentence = inFromServer.readLine();

        System.out.println(modifiedSentence);

        System.out.println("After Color Change");

        modifiedSentence = inFromServer.readLine();

        System.out.println(modifiedSentence);

    }
}

