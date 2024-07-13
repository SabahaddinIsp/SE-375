import javax.management.StringValueExp;
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
import java.net.*;
import java.io.*;
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

        private String url;

        public allThread(String nameC, String nameca,int times , ConcurrentHashMap<Integer, String[]> a,Lock lock1,String u) {

            this.nameCol = nameC;

            this.nameCase = nameca;

            this.timesIter = times;

            this.tmpMap1 = a;

            this.lock = lock1;

            this.url = u;

            try {

                BufferedInputStream in = new BufferedInputStream(new URL(u).openStream());

                String[] t = new String[20];

                String[] tReal = new String[20];

                for(int i =0;i<20;i++){

                    t[i] =String.valueOf ((char)in.read());

                }

                for(int i =0;i<20;i++){

                    tReal[0] = t[i];

                    a.put(i,tReal);

                }

            } catch (IOException e) {

                e.printStackTrace();

            }

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

        final Lock lock = new ReentrantLock();


        ArrayList<ConcurrentHashMap<Integer,String[]>> hold = new ArrayList<>();

        ConcurrentHashMap<Integer,String[]> holder1 = new ConcurrentHashMap<>();

        ConcurrentHashMap<Integer,String[]> holder2 = new ConcurrentHashMap<>();

        ConcurrentHashMap<Integer,String[]> holder3 = new ConcurrentHashMap<>();

        ConcurrentHashMap<Integer,String[]> holder4 = new ConcurrentHashMap<>();

        hold.add(holder1);

        hold.add(holder2);

        hold.add(holder3);

        hold.add(holder4);

        ArrayList<String> hold2 = new ArrayList<>();

        String url1 = "http://homes.ieu.edu.tr/eokur/sample1.txt";

        String url2 = "http://homes.ieu.edu.tr/eokur/sample1.txt";

        String url3 = "http://homes.ieu.edu.tr/eokur/sample1.txt";

        String url4 = "http://homes.ieu.edu.tr/eokur/sample1.txt";

        hold2.add(url1);

        hold2.add(url2);

        hold2.add(url3);

        hold2.add(url4);

        String ans1 ;

        ans1 = "U";

        String ans3;

        ans3 = "R";

        int ans2;

        ans2 = 1;

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for(int j = 0; j<4;j++){

            ConcurrentHashMap<Integer,String[]> tmpMap = hold.get(j);

            String tmpUrl = hold2.get(j);

            executorService.execute(new Thread(new allThread(ans3,ans1,ans2,tmpMap,lock,url1)));

        }

        executorService.shutdown();

        for(int j = 0;j<4;j++){

            ConcurrentHashMap<Integer,String[]> tmpMap = hold.get(j);

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

