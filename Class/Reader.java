package Class;

import java.util.ArrayList;
import java.util.Random;


public class Reader implements Runnable {
    private String registrador = "";    //reader's local variable
    private Base database;
    private int readerNum;

    public Reader(int readerNum, Base database) {
        this.readerNum = readerNum;
        this.database = database;
    }

    public void read(ArrayList<String> wordlist, int i) {
        //change the Reader's local variable
        registrador = wordlist.get(i);
    }

    @Override
    public void run() {
        //System.out.println("reader " + readerNum + " wants to read.");
        database.acquireReadLock(readerNum);

        // you have access to read from the database
        Random gerador = new Random();
        synchronized (database.getWordlist()) {
            for (int i = 0; i < 100; i++) {
                read(database.getWordlist(), gerador.nextInt(101));
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }

        database.releaseReadLock(readerNum);
    }
}