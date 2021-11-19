package Class;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Base {

    //http://www2.hawaii.edu/~walbritt/ics240/synchronization/ReaderWriterSolution.java

    private int readerCount;  // the number of active readers
    private Semaphore mutex;  // controls access to readerCount
    private Semaphore db;     // controls access to the database
    private ArrayList<String> wordlist;


    public Base(ArrayList<String> wordlist) {
        readerCount = 0;
        mutex = new Semaphore(1);
        db = new Semaphore(1);
        this.wordlist = wordlist;
    }

    public void acquireReadLock(int readerNum) {
        try {
            //mutual exclusion for readerCount
            mutex.acquire();
        } catch (InterruptedException e) {
        }

        readerCount++;

        // if I am the first reader tell all others
        // that the database is being read
        if (readerCount == 1) {
            try {
                db.acquire();
            } catch (InterruptedException e) {
            }
        }

        //System.out.println("Reader " + readerNum + " is reading. Reader count = " + readerCount);
        //mutual exclusion for readerCount
        mutex.release();
    }

    public void releaseReadLock(int readerNum) {
        try {
            //mutual exclusion for readerCount
            mutex.acquire();
        } catch (InterruptedException e) {
        }

        readerCount--;

        // if I am the last reader tell all others
        // that the database is no longer being read
        if (readerCount == 0) {
            db.release();
        }

        //System.out.println("Reader " + readerNum + " is done reading. Reader count = " + readerCount);

        //mutual exclusion for readerCount
        mutex.release();
    }

    public void acquireWriteLock(int writerNum) {
        try {
            db.acquire();
        } catch (InterruptedException e) {
        }
        //System.out.println("Writer " + writerNum + " is writing.");
    }

    public void releaseWriteLock(int writerNum) {
        //System.out.println("Writer " + writerNum + " is done writing.");
        db.release();
    }

    public ArrayList<String> getWordlist() {
        return wordlist;
    }

    public void setWordlist(ArrayList<String> wordlist) {
        this.wordlist = wordlist;
    }

}
