import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.*;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
public class Client {

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

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

        byte[] enc;

        byte[] encryptedText;

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

        System.out.println("Received String URL: "+ cipherUrl);

        decrypted_text = Base64.getDecoder().decode(cipherUrl);

        decrypted_text1 = cipher.doFinal(decrypted_text);

        BufferedInputStream in = new BufferedInputStream(new URL(new String(decrypted_text1)).openStream());

        for(int i = in.available();i>0;i--)

            urlData = urlData + (char)in.read();

        in.close();

        System.out.println("Data: "+urlData);

        System.out.println("Enter Choices: U/L How many R/Y");

        UL = inFromUser.readLine();

        times = inFromUser.readLine();

        RY = inFromUser.readLine();

        outToServer.writeUTF(urlData.length()+" "+UL+" "+times+" "+RY);

        char[] holder = urlData.toCharArray();

        cipher.init(Cipher.ENCRYPT_MODE, originalKey);

        String holderCh;

        for(int i=0; i<urlData.length();i++){

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