import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;

public class Server {

    public static void main(String argv[]) throws Exception {

        String clientSentence;

        String capitalizedSentence;

        String dataL;

        Cipher cipher;

        byte[] encrypted_text;

        byte[] decrypted_text;

        byte[] decrypted_text1;

        String url = "https://homes.izmirekonomi.edu.tr/eokur/sample0.txt";

        ServerSocket welcomeSocket = new ServerSocket(6789);

        Socket connectionSocket = welcomeSocket.accept();

        DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());

        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

        System.out.println("Waiting For Connection...");

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");

        SecureRandom secureRandom = new SecureRandom();

        keyGen.init(256, secureRandom);

        SecretKey secretKey = keyGen.generateKey();

        byte[] secretKeyHolder = secretKey.getEncoded();

        String StringSecretKey = Base64.getEncoder().encodeToString(secretKeyHolder);

        outToClient.writeUTF(StringSecretKey);

        System.out.println("Secret Key has sent ");

        cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        encrypted_text = cipher.doFinal(url.getBytes(StandardCharsets.UTF_8));

        System.out.println("Sending the URL");

        outToClient.writeUTF(Base64.getEncoder().encodeToString(encrypted_text));

        clientSentence = inFromClient.readUTF();

        String[] hold = clientSentence.split(" ");

        dataL = hold[0];

        System.out.println(dataL);

        String[] holder = new String[20];

        int intVal = Integer.parseInt(dataL);

        String comingString;

        for (int i = 0; i < intVal; i++) {

            comingString = inFromClient.readUTF();

            decrypted_text = Base64.getDecoder().decode(comingString);

            decrypted_text1 = cipher.doFinal(decrypted_text);

            holder[i] = new String(decrypted_text1);

        }

        if (hold[1].equals("U")) {

            for (int i = 0; i < intVal; i++) {

                holder[i] = holder[i].toUpperCase(Locale.ROOT);

            }

        } else if (hold[1].equals("L")) {

            for (int i = 0; i < intVal; i++) {

                holder[i] = holder[i].toLowerCase(Locale.ROOT);

            }

        }

        String tmp = "";

        for (int i = 0; i < intVal; i++) {

            tmp = tmp + holder[i];

        }

        outToClient.writeUTF(tmp);

        for (int i = 0; i < intVal; i++) {

            String str1 = holder[i];

            char c = str1.charAt(0);

            for (int d = 0; d < Integer.parseInt(hold[2]); d++) {

                c++;


            }

            holder[i] = String.valueOf(c);

        }

        tmp = "";

        for (int i = 0; i < intVal; i++) {

            tmp = tmp + holder[i];

        }

        outToClient.writeUTF(tmp);

        final String ANSI_RESET = "\u001B[0m";

        final String ANSI_RED = "\u001B[31m";

        final String ANSI_YELLOW = "\u001B[33m";

        if (hold[3].equals("R")) {

            for (int i = 0; i < intVal; i++) {

                String tmpColored1 = new String(ANSI_RED + holder[i] + ANSI_RESET);

                holder[i] = tmpColored1;

            }

        } else if (hold[3].equals("Y")) {

            for (int i = 0; i < intVal; i++) {

                String tmpColored1 = new String(ANSI_YELLOW + holder[i] + ANSI_RESET);

                holder[i] = tmpColored1;

            }

        }

        tmp = "";

        for (int i = 0; i < intVal; i++) {

            tmp = tmp + holder[i];

        }

        outToClient.writeUTF(tmp);

    }
}