package Class;

import java.util.ArrayList;
import java.util.Random;

public class Writer implements Runnable {

    private Base database;
    private int writerNum;

    public Writer(int w, Base d) {
        writerNum = w;
        database = d;
    }

    public void write(ArrayList<String> wordlist, int i) {
        //change the element "i" of "wordlist" for the value "MODIFICADO"
        wordlist.set(i, "MODIFICADO");
        database.setWordlist(wordlist);
    }

    @Override
    public void run() {
        //System.out.println("writer " + writerNum + " wants to write.");
        database.acquireWriteLock(writerNum);

        // you have access to write to the database
        Random gerador = new Random();

        synchronized (database.getWordlist()) {
            for (int i = 0; i < 100; i++) {
                write(database.getWordlist(), gerador.nextInt(36242));
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }

        database.releaseWriteLock(writerNum);
    }
}