import java.net.*;
import java.io.*;
import java.util.Locale;

public class Server {

    public static void main(String[] argv) throws Exception {

        String clientSentence;

        String capitalizedSentence;

        String dataL;

        ServerSocket welcomeSocket = new ServerSocket(6789);

        Socket connectionSocket = welcomeSocket.accept();

        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

        System.out.println("Waiting For Connection...");

        System.out.println("Sending the URL");
        outToClient.writeBytes("https://homes.izmirekonomi.edu.tr/eokur/sample0.txt\n");

        clientSentence = inFromClient.readLine();

        String[] hold = clientSentence.split(" ");

        dataL = hold[0];

        System.out.println(dataL);

        String[] holder = new String[20];

        int intVal = Integer.parseInt(dataL);

        for (int i = 0; i < intVal; i++) {

            holder[i] = inFromClient.readLine();

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

        outToClient.writeChars(tmp + "\n");

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

        outToClient.writeChars(tmp + "\n");

        final String ANSI_RESET = "\u001B[0m";

        final String ANSI_RED = "\u001B[31m";

        final String ANSI_YELLOW = "\u001B[33m";

        if (hold[3].equals("R")) {

            for (int i = 0; i < intVal; i++) {

                String tmpColored1 = ANSI_RED + holder[i] + ANSI_RESET;

                holder[i] = tmpColored1;

            }

        } else if (hold[3].equals("Y")) {

            for (int i = 0; i < intVal; i++) {

                String tmpColored1 = ANSI_YELLOW + holder[i] + ANSI_RESET;

                holder[i] = tmpColored1;

            }

        }

        tmp = "";

        for (int i = 0; i < intVal; i++) {

            tmp = tmp + holder[i];

        }

        outToClient.writeChars(tmp + "\n");

    }
}

