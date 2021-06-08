import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.io.BufferedWriter;
import java.util.concurrent.locks.ReentrantLock;

class MyThread implements Runnable {
    String name;
    Thread t;
    String text;
    String[] words;
    ReentrantLock mutex;
    Semaphore semaphore;
    //true for semaphore and false for mutex_lock
    boolean type;

    MyThread(String threadNumber, String text, String[] arr_words, ReentrantLock mutex, Semaphore semaphore, boolean type) {
        name = threadNumber;
        this.text = text;
        words = arr_words;
        this.mutex = mutex;
        this.type = type;
        this.semaphore = semaphore;
        t = new Thread(this, name);
        t.start();
    }


    public void run() {
        long startTime = System.currentTimeMillis();
        System.out.println("crate thread " + name + " and text is: " + text + "--->search words are: " + Arrays.toString(words));
        //--------------------------------------------------------------------------------------------------------------

        String[] keys = text.split(" ");
        root = new TrieNode();
        int currentPlace = 1;
        for (String key : keys) {
            insert(key, currentPlace);
            currentPlace += key.length();
        }
        for (String word : words) {
            ArrayList<Integer> res = search(word);
            if (res != null) {
                long endTime = System.currentTimeMillis();
                //System.out.println("------------->" + word + " found in thread " + name + " : " + search(word) + " and find time is :" + (endTime - startTime));
                String myText = word + " found in thread " + name + " : " + res + " and find time is :" + (endTime - startTime);
                System.out.println(myText);
                /*
                    now we should use semaphore and mutex lock here.
                 */
                if (!type) {
                    //mutex lock
                    try {
                        mutex.lock();
                        long endWrite = System.currentTimeMillis();
                        myText += " and write in file in " + (endWrite - startTime) + "\n";
                        FileWriter fileWritter = new FileWriter("output.txt", true);
                        BufferedWriter bw = new BufferedWriter(fileWritter);
                        bw.write(myText);
                        bw.close();


                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }


                    mutex.unlock();

                } else {
                    //semaphore
                    try {
                        System.out.println("the semaphore avalible in thread " + name + " is " + semaphore.availablePermits());
                        //start time for waiting
                        long waitTime = System.currentTimeMillis();
                        // Aquire a permit to proceed
                        semaphore.acquire();
                        long enterTime = System.currentTimeMillis();
                        myText += " and wait to get the semaphore " + (enterTime - waitTime);
                        FileWriter fileWritter = new FileWriter("output.txt", true);

                        BufferedWriter bw = new BufferedWriter(fileWritter);
                        bw.write(myText);
                        long endWrite = System.currentTimeMillis();
                        String newText = " write in " + (endWrite - startTime) + "\n";
                        bw.write(newText);
                        bw.close();

                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    } finally {
                        // Always release our permit
                        semaphore.release();
                    }


                }
            }

        }
    }


    TrieNode root;

    void insert(String word, int occurence) {
        int letter;
        TrieNode current = root;

        for (int i = 0; i < word.length(); i++) {
            letter = word.charAt(i) - 97;
            if (current.children[letter] == null)
                current.children[letter] = new TrieNode();

            current = current.children[letter];
        }
        current.isEndOfWord = true;
        current.occurences.add(occurence);
    }

    ArrayList<Integer> search(String word) {
        int letter;
        TrieNode current = root;

        for (int i = 0; i < word.length(); i++) {
            letter = word.charAt(i) - 97;
            if (current.children[letter] == null)
                return null;
            current = current.children[letter];
        }

        if (current != null && current.isEndOfWord)
            return current.occurences;
        return null;
    }
}

public class Main {


    public static void main(String[] args) {
        //Semaphore mutex = new Semaphore(1);
        ReentrantLock mutex = new ReentrantLock();
        Semaphore semaphore = new Semaphore(1);
        try {
            File f1 = new File("output.txt");
            if (f1.exists()) {
                FileWriter myWriter = new FileWriter("output.txt");
                myWriter.write("");
                myWriter.close();
            }

            File input_file = new File("input.txt");
            Scanner scan = new Scanner(input_file);
            StringBuilder data = new StringBuilder();
            //read data from file and put in string.
            while (scan.hasNextLine()) {
                data.append(scan.nextLine());
            }
            scan.close();
            //split words in text and remove dots
            String text = data.toString();
            String[] arr_of_words = text.split(" ");

            for (int i = 0; i < arr_of_words.length; i++) {
                arr_of_words[i] = arr_of_words[i].toLowerCase(Locale.ROOT);
                String temp = arr_of_words[i];
                String word_without_dot = "";
                for (int j = 0; j < temp.length(); j++) {

                    if (temp.charAt(j) == '.' || temp.charAt(j) == ',' || temp.charAt(j) == '\"') {
                    } else {
                        word_without_dot += temp.charAt(j);
                    }
                }
                arr_of_words[i] = word_without_dot;
            }
            System.out.println(Arrays.toString(arr_of_words));
            System.out.println(arr_of_words.length);
            String text1 = "", text2 = "", text3 = "", text4 = "";
            for (int i = 0; i < arr_of_words.length / 4; i++) {
                text1 += arr_of_words[i];
                text1 += " ";
            }
            for (int i = arr_of_words.length / 4; i < 2 * (arr_of_words.length / 4); i++) {
                text2 += arr_of_words[i];
                text2 += " ";
            }
            for (int i = 2 * (arr_of_words.length / 4); i < 3 * (arr_of_words.length / 4); i++) {
                text3 += arr_of_words[i];
                text3 += " ";
            }
            for (int i = 3 * (arr_of_words.length / 4); i < arr_of_words.length; i++) {
                text4 += arr_of_words[i];
                text4 += " ";
            }
            System.out.println("This is text that split it in 4 part(depend on count of words):");
            System.out.println(text1);
            System.out.println(text2);
            System.out.println(text3);
            System.out.println(text4);
            //read search words
            File search_file = new File("words.txt");
            scan = new Scanner(search_file);
            StringBuilder words_in_file = new StringBuilder();
            //read data from file and put in string.
            while (scan.hasNextLine()) {
                words_in_file.append(scan.nextLine());
                words_in_file.append(" ");
            }
            String words = words_in_file.toString();
            words = words.toLowerCase(Locale.ROOT);
            scan.close();
            String[] arr_of_search_words = words.split(" ");
            System.out.println("********************************************");
            //true for semaphore and false for mutex_lock
            boolean type = true;
            new MyThread("One", text1, arr_of_search_words, mutex, semaphore, type);
            new MyThread("Two", text2, arr_of_search_words, mutex, semaphore, type);
            new MyThread("Three", text3, arr_of_search_words, mutex, semaphore, type);
            new MyThread("Four", text4, arr_of_search_words, mutex, semaphore, type);
        } catch (FileNotFoundException e) {
            System.out.println("Error in open file!!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}

class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord;
    ArrayList<Integer> occurences;

    TrieNode() {
        isEndOfWord = false;
        occurences = new ArrayList<>();
        for (int i = 0; i < 26; i++)
            children[i] = null;
    }
}