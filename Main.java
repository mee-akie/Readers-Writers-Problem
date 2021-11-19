import Class.Base;
import Class.FileHandler;
import Class.Reader;
import Class.Writer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static List<String> wordsBd = new ArrayList<>();
    public static Thread[] objects = new Thread[100];
    public static Object[] objects2 = new Object[100];

    public static void main(String[] args) {

        //file which contains the words
        File fileWordlist = new File("bd.txt");

        //reading and stored the words from file bd.txt
        FileHandler handler = new FileHandler();
        wordsBd = handler.fileReader(fileWordlist);

        //if had errors reading the file, the process ends
        if (wordsBd == null) return;

        //readerWriterAlgorithm();

        nonReaderWriterAlgorithm();


    }


    /*************************************************************
     *  Funcao responsavel por criar as instancias de Writer e   *
     *  Reader e aloca-los em um arranjo de Threads              *
     *************************************************************/

    private static void storageThreads(int readers, Base database) {
        //index will be storage all empty index in objects
        ArrayList<Integer> emptyIndex = new ArrayList<Integer>();

        //add all empty index in index
        for (int i = 0; i < 100; i++)
            emptyIndex.add(i);

        //random number generator, used for generate emptyIndex index
        ThreadLocalRandom generator = ThreadLocalRandom.current();

        //auxiliar variable to know what kind of object it will be created (Reader or Writer)
        int numReaders = readers;
        int numWriters = Math.abs(numReaders - 100);

        //creating 100 objects (threads) and storage in ArrayList objects
        for (int i = 0; i < 100; i++) {

            //find a random existing index in emptyIndex
            int indexAux = generator.nextInt(0, emptyIndex.size());

            //storage in local variable a random empty index of objects
            int indexObject = emptyIndex.get(indexAux);

            //storage the thread in emptyIndex of objects
            if (numReaders > 0) {
                objects[indexObject] = new Thread(new Reader(numReaders, database));
                objects2[indexObject] = new Reader(numReaders, database);
                numReaders--;
            } else {
                objects[indexObject] = new Thread(new Writer(numWriters, database));
                objects2[indexObject] = new Writer(numReaders, database);
                numWriters--;
            }

            //remove index of emptyIndex (because now this index is ocupated)
            emptyIndex.remove(indexAux);

        }
    }


    /**************************************************************
     *   Algoritmo para o problema de Leitores e Escritores.      *
     *   Para essa solucao, foi utilizado uma solucao ja pronta,  *
     *   porem parte dela foi modificada para esse EP             *
     **************************************************************/

    private static void readerWriterAlgorithm() {
        //Proporcao de Writers and Readers: [Readers = 0 and Writers = 100], [Readers = 1 and Writers = 99], [...], [Readers = 100 and Writers = 0]
        for (int i = 0; i < 101; i++) {
            Base database = new Base((ArrayList<String>) wordsBd);
            storageThreads(i, database);

            long time = System.currentTimeMillis();

            // Inicializa as Threads de forma sequencial de acordo com sua posicao no arranjo
            for (int j = 0; j < 100; j++) {
                objects[j].start();
            }

            // Verifica se todas as Threads ja foram concluidas. Caso nao, o sistema espera a
            // conclusao das Threads ainda ativas
            for (int k = 0; k < 100; k++) {
                if (objects[k].isAlive()) {
                    try {
                        objects[k].join();
                    } catch (InterruptedException e) {
                    }
                }
            }

            // tempo total decorrido desde o inicio da primeira Thread ate a conclusao da ultima
            time = System.currentTimeMillis() - time;

            System.out.println(time);
        }
    }


    /*****************************************************
     *   Algoritmo que NAO utiliza a implementacao de    *
     *   Leitore e Escritore.                            *
     *****************************************************/

    private static void nonReaderWriterAlgorithm() {
        //Proporcao de Writers and Readers: [Readers = 0 and Writers = 100], [Readers = 1 and Writers = 99], [...], [Readers = 100 and Writers = 0]
        for (int i = 0; i < 101; i++) {

            Base database = new Base((ArrayList<String>) wordsBd);
            storageThreads(i, database);

            long time = System.currentTimeMillis();

            // Inicializa as Threads de forma sequencial de acordo com sua posicao no arranjo
            for (int j = 0; j < 100; j++) {

                Object o = objects2[j];

                if (o.getClass().equals(Writer.class)) {
                    ((Writer) o).write((ArrayList<String>) wordsBd, j);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                } else {
                    ((Reader) o).read((ArrayList<String>) wordsBd, j);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }

            }

            // tempo total decorrido desde o inicio da primeira Thread ate a conclusao da ultima
            time = System.currentTimeMillis() - time;

            System.out.println(time);
        }
    }

}
