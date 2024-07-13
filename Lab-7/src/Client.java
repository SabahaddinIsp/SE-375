import java.util.*;
import java.net.*;
import java.io.*;
import java.security.MessageDigest;
import java.nio.charset.*;
import javax.crypto.*;
import javax.crypto.spec.*;
public class Client{

    public static void main(String[] args) throws Exception{

        String UL;

        String times;

        String RY;

        String cipherUrl;

        String urlData = "";

        String modifiedSentence;

        String secretKey;

        Cipher cipher;

        byte[] decrypted_text;

        byte[] decrypted_text1;

        byte[] encryptedText;

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket("localhost", 6789);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());

        secretKey = inFromServer.readUTF();

        byte[] encodedKey = Base64.getDecoder().decode(secretKey);

        SecretKey originalKey = new SecretKeySpec(encodedKey,"AES");

        cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE,originalKey);

        cipherUrl = inFromServer.readUTF();

        String U;

        decrypted_text = Base64.getDecoder().decode(cipherUrl);

        decrypted_text1 = cipher.doFinal(decrypted_text);

        U = new String(decrypted_text1);

        System.out.println("Received String URL: "+ U);

        byte[] hashInBytes = md.digest(U.getBytes(StandardCharsets.UTF_8));

        byte tmp;

        boolean flag = true;

        for(int i = 0;i< hashInBytes.length;i++){

            tmp = inFromServer.readByte();

            if(tmp!= hashInBytes[i])

                flag = false;

        }

        if (!flag){

            System.out.println("Closing Connection");

            clientSocket.close();

        }

        else

            System.out.println("Url digest checked");

        decrypted_text1 = cipher.doFinal(decrypted_text);

        System.out.println(decrypted_text1);

        BufferedInputStream in = new BufferedInputStream(new URL(new String(decrypted_text1)).openStream());

        for(int i=in.available(); i>0; i--)

            urlData+=(char)in.read();

        in.close();

        System.out.println("Data: "+urlData);

        System.out.println("Enter Choices: U/L Shift (number) R/Y");

        UL = inFromUser.readLine();

        times = inFromUser.readLine();

        RY = inFromUser.readLine();

        String str = urlData.length() + " " + UL + " " + times + " " + RY;

        outToServer.writeUTF(str);

        byte[] preferences = md.digest((str).getBytes(StandardCharsets.UTF_8));

        for(byte b: preferences)

            outToServer.writeByte(b);

        char[] holder = urlData.toCharArray();

        cipher.init(Cipher.ENCRYPT_MODE, originalKey);

        String holderCh;

        for(int i=0; i<urlData.length(); i++){

            holderCh = String.valueOf(holder[i]);

            encryptedText = cipher.doFinal(holderCh.getBytes(StandardCharsets.UTF_8));

            outToServer.writeUTF(Base64.getEncoder().encodeToString(encryptedText));

        }

        System.out.println("ORIGINAL: "+ urlData);


        System.out.println("After Case Change");

        modifiedSentence = inFromServer.readUTF();

        System.out.println(modifiedSentence);

        System.out.println("After Shift Change");

        modifiedSentence = inFromServer.readUTF();

        System.out.println(modifiedSentence);

        System.out.println("After Color Change");

        modifiedSentence = inFromServer.readUTF();

        System.out.println(modifiedSentence);

    }
}