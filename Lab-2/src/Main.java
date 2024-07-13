import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        FileReader file = new FileReader("C:\\Users\\ML105\\IdeaProjects\\Lab1\\src\\13079745.txt");
        char[] tmp = new char[20];
        file.read(tmp);
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < tmp.length; i++)
            System.out.print(tmp[i]);
        System.out.println();
        HashMap<Integer, String[]> tmpMap = new HashMap<>();
        for (int i = 0; i < tmp.length; i++) {
            String[] tmpSt = new String[4];
            int stVal = 0;
            tmpSt[stVal] = String.valueOf(tmp[i]);
            stVal++;
            tmpSt[stVal] = null;
            stVal++;
            tmpSt[stVal] = null;
            stVal++;
            tmpSt[stVal] = null;
            tmpMap.put(i, tmpSt);
        }
        System.out.println("Please state your choice...\n" +
                "UPPER case or lower case (U or L):");
        String ans1 = sc.nextLine();
        Thread tCase = new Thread(new caseThread(ans1, tmpMap));
        tCase.start();
        System.out.println("Please state your choice...\n" +
                "Color of characters (R or Y): ");
        String ans3 = sc.nextLine();
        Thread tColor = new Thread(new colorThread(ans3, tmpMap));
        tColor.start();
        System.out.println("Please state your choice...\n" +
                "How many characters to shift (number between 1-3): ");
        int ans2 = sc.nextInt();
        Thread tShift = new Thread(new shiftThread(ans2, tmpMap));
        tShift.start();
        tCase.join();
        tColor.join();
        tShift.join();
        for (int i = 0; i < tmpMap.size(); i++) {
            System.out.print(i + "\t--> " + tmpMap.get(i)[0]);
            System.out.print("\t" + tmpMap.get(i)[1]);
            System.out.print("\t" + tmpMap.get(i)[2]);
            System.out.println("\t" + tmpMap.get(i)[3]);
        }
    }

    public static class caseThread extends Thread {
        private final String name;
        private final HashMap<Integer, String[]> tmpMap1;

        public caseThread(String s, HashMap<Integer, String[]> a) {
            this.name = s;
            this.tmpMap1 = a;
        }

        @Override
        public void run() {
            if (name.equals("U")) {
                for (int i = 0; i < tmpMap1.size(); i++) {
                    tmpMap1.get(i)[1] = tmpMap1.get(i)[0].toUpperCase(Locale.ROOT);
                }
            } else if (name.equals("L")) {
                for (int i = 0; i < tmpMap1.size(); i++) {
                    tmpMap1.get(i)[1] = tmpMap1.get(i)[0].toLowerCase(Locale.ROOT);
                }
            }
        }
    }

    public static class shiftThread extends Thread {
        private final int times;
        private final HashMap<Integer, String[]> tmpMap1;

        public shiftThread(int s, HashMap<Integer, String[]> a) {
            this.times = s;
            this.tmpMap1 = a;
        }

        @Override
        public void run() {
            for (int i = 0; i < tmpMap1.size(); i++) {
                String str1 = tmpMap1.get(i)[0];
                char c = str1.charAt(0);
                for (int d = 0; d < times; d++) {
                    c++;
                }
                tmpMap1.get(i)[2] = String.valueOf(c);
            }
        }
    }

    public static class colorThread extends Thread {
        private final String name;
        private final HashMap<Integer, String[]> tmpMap1;

        public colorThread(String s, HashMap<Integer, String[]> a) {
            this.name = s;
            this.tmpMap1 = a;
        }

        @Override
        public void run() {
            final String ANSI_RESET = "\u001B[0m";
            final String ANSI_RED = "\u001B[31m";
            final String ANSI_YELLOW = "\u001B[33m";
            if (name.equals("R")) {
                for (int i = 0; i < tmpMap1.size(); i++) {
                    String tmpColored1 = ANSI_RED + tmpMap1.get(i)[1] + ANSI_RESET;
                    tmpMap1.get(i)[3] = tmpColored1;
                }
            } else if (name.equals("Y")) {
                for (int i = 0; i < tmpMap1.size(); i++) {
                    String tmpColored1 = ANSI_YELLOW + tmpMap1.get(i)[1] + ANSI_RESET;
                    tmpMap1.get(i)[3] = tmpColored1;

                }
            }
        }
    }
}