package Class;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {

    public List<String> fileReader(File file) {

        List<String> wordList = new ArrayList<>();

        try {
            //make de bd.txt "readable"
            Scanner sc = new Scanner(new FileReader(file.getName()));

            //read the lines of bd.txt file
            while (sc.hasNext()) {
                String line = sc.nextLine();
                wordList.add(line);
            }
            return wordList;
        } catch (FileNotFoundException e) {
            e.getStackTrace(); //error message
            return null;
        }
    }
}