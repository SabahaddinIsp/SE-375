import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class Main {

    public static class caseThread extends Thread{

        private String name;

        private ConcurrentHashMap<Integer,String[]> tmpMap1;

        private Lock lock;

        public caseThread(String s, ConcurrentHashMap<Integer, String[]> a, Lock lock1) {

            this.name = s;

            this.tmpMap1 = a;

            this.lock = lock1;

        }

        @Override

        public void run(){

            if (name.equals("U")) {

                for (int i = 0; i < tmpMap1.size(); i++) {

                    tmpMap1.get(i)[1] = tmpMap1.get(i)[0].toUpperCase(Locale.ROOT);

                    lock.lock();

                    tmpMap1.get(i)[4] = String.valueOf(Integer.parseInt(tmpMap1.get(i)[4]) + 1);

                    lock.unlock();

                }

            } else if (name.equals("L")) {

                for (int i = 0; i < tmpMap1.size(); i++) {

                    tmpMap1.get(i)[1] = tmpMap1.get(i)[0].toLowerCase(Locale.ROOT);

                    lock.lock();

                    tmpMap1.get(i)[4] = String.valueOf(Integer.parseInt(tmpMap1.get(i)[4]) + 1);

                    lock.unlock();

                }

            }

        }

    }

    public static class shiftThread extends Thread{

        private int times;

        private ConcurrentHashMap<Integer,String[]> tmpMap1;

        private Lock lock;

        public shiftThread(int s, ConcurrentHashMap<Integer, String[]> a, Lock lock1) {

            this.times = s;

            this.tmpMap1 = a;

            this.lock = lock1;

        }

        @Override

        public void run(){

            for(int i = 0;i< tmpMap1.size();i++) {

                String str1 = tmpMap1.get(i)[0];

                char c = str1.charAt(0);

                for (int d = 0; d < times; d++) {

                    c++;

                }

                tmpMap1.get(i)[2] = String.valueOf(c);

                lock.lock();

                tmpMap1.get(i)[4] = String.valueOf(Integer.parseInt(tmpMap1.get(i)[4]) + 1);

                lock.unlock();

            }

        }

    }

    public static class colorThread extends Thread{

        private String name;

        private ConcurrentHashMap<Integer,String[]> tmpMap1;

        private Lock lock;

        public colorThread(String s, ConcurrentHashMap<Integer, String[]> a, Lock lock1) {

            this.name = s;

            this.tmpMap1 = a;

            this.lock = lock1;

        }

        @Override

        public void run(){

            final String ANSI_RESET = "\u001B[0m";

            final String ANSI_RED = "\u001B[31m";

            final String ANSI_YELLOW = "\u001B[33m";

            if (name.equals("R")) {

                for (int i = 0; i < tmpMap1.size(); i++) {

                    String tmpColored1 = new String(ANSI_RED + tmpMap1.get(i)[1] + ANSI_RESET);

                    tmpMap1.get(i)[3] = tmpColored1;

                    lock.lock();


                    tmpMap1.get(i)[4] = String.valueOf(Integer.parseInt(tmpMap1.get(i)[4]) + 1);

                    lock.unlock();

                }

            } else if (name.equals("Y")) {

                for (int i = 0; i < tmpMap1.size(); i++) {

                    String tmpColored1 = new String(ANSI_YELLOW + tmpMap1.get(i)[1] + ANSI_RESET);

                    tmpMap1.get(i)[3] = tmpColored1;

                    lock.lock();

                    tmpMap1.get(i)[4] = String.valueOf(Integer.parseInt(tmpMap1.get(i)[4]) + 1);

                    lock.unlock();

                }

            }

        }

    }

    public static class allThread extends Thread {

        private String nameCol;

        private String nameCase;

        private int timesIter;

        private ConcurrentHashMap<Integer,String[]> tmpMap1;

        private int cur;

        private Lock lock;

        public allThread(String nameC, String nameca,int times , ConcurrentHashMap<Integer, String[]> a,Lock lock1) {

            this.nameCol = nameC;

            this.nameCase = nameca;

            this.timesIter = times;

            this.tmpMap1 = a;

            this.lock = lock1;

        }

        @Override

        public void run(){

            Thread tCase = new Thread(new caseThread(nameCase,tmpMap1,lock));

            tCase.start();

            Thread tColor = new Thread(new colorThread(nameCol,tmpMap1,lock));

            tColor.start();

            Thread tShift = new Thread(new shiftThread(timesIter,tmpMap1,lock));

            tShift.start();

            try {

                tCase.join();

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            try {

                tColor.join();

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            try {

                tShift.join();

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {

        FileReader file = new FileReader("C:\\Users\\ML105\\IdeaProjects\\Lab1\\src\\2.txt");

        char tmp[] = new char[21];

        file.read(tmp);

        final Lock lock = new ReentrantLock();

        String[] files = new String[4];

        files[0] = "C:\\Users\\ML105\\IdeaProjects\\Lab1\\src\\1.txt";

        files[1] = "C:\\Users\\ML105\\IdeaProjects\\Lab1\\src\\2.txt";

        files[2] = "C:\\Users\\ML105\\IdeaProjects\\Lab1\\src\\3.txt";

        files[3] = "C:\\Users\\ML105\\IdeaProjects\\Lab1\\src\\4.txt";

        Scanner sc = new Scanner(System.in);
/*
 
ArrayList< ConcurrentHashMap<Integer,String[]>> holder = new ArrayList<>();
 
ConcurrentHashMap<Integer,String[]> tmpMap;
 
FileReader file;
 
char tmp[];
 
for(int i =0;i<files.length;i++){
 
file = new FileReader(files[i]);
 
tmp = new char[21];
 
file.read(tmp);
 
tmpMap = new ConcurrentHashMap<>();
 
for(int j= 0; j< tmp.length;j++){
 
String[] tmpSt = new String[5];

 
tmpSt[0] = String.valueOf(tmp[j]);
 
tmpSt[4] = "0";
 
tmpMap.put(j,tmpSt);
 
}
 
holder.add(tmpMap);
 
}
 */

        ConcurrentHashMap<Integer,String[]> tmpMap = new ConcurrentHashMap<>();

        for(int i= 0; i< tmp.length;i++){

            String[] tmpSt = new String[5];

            tmpSt[0] = String.valueOf(tmp[i]);

            int stVal = 0;

            tmpSt[4] = "0";

            tmpMap.put(i,tmpSt);

        }

        System.out.println("Please state your choice...\n" +

                "UPPER case or lower case (U or L):");

        String ans1 = sc.nextLine();

        System.out.println("Please state your choice...\n" +

                "Color of characters (R or Y): ");

        String ans3 = sc.nextLine();

        System.out.println("Please state your choice...\n" +

                "How many characters to shift (number between 1-3): ");

        int ans2 = sc.nextInt();

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for(int j = 0; j<4;j++){

            executorService.execute(new Thread(new allThread(ans3,ans1,ans2,tmpMap,lock)));

            for (int i = 0; i< tmpMap.size();i++){

                System.out.print(i+"\t--> "+tmpMap.get(i)[0]);

                System.out.print("\t"+ tmpMap.get(i)[1]);

                System.out.print("\t"+tmpMap.get(i)[2]);

                System.out.print("\t"+tmpMap.get(i)[3]);

                System.out.println("\t"+tmpMap.get(i)[4]);

            }

        }

    }
}

