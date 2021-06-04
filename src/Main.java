import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

class MyThread implements Runnable {
    String name;
    Thread t;
    String text;
    String[] words;

    MyThread(String threadNumber, String text, String[] arr_words) {
        name = threadNumber;
        this.text = text;
        words = arr_words;
        t = new Thread(this, name);
        t.start();
    }


    public void run() {
        System.out.println("crate thread " + name + " and text is: " + text + "--->search words are: " + Arrays.toString(words));
        //--------------------------------------------------------------------------------------------------------------

        String keys[] = text.split(" ");
        root = new TrieNode();
        int currentPlace = 1;
        for (int i = 0; i < keys.length; i++) {
            insert(keys[i], currentPlace);
            currentPlace += keys[i].length();
        }
        for (int i = 0; i < words.length; i++) {
            System.out.println("------------->" + words[i] + " found in thread " + name + " : " + search(words[i]));
        }

        System.out.println(name + " exiting.");
    }

    static TrieNode root;

    static void insert(String word, int occurence) {
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

    static ArrayList<Integer> search(String word) {
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

    public void run(int num) {
        System.out.println("This code is running in a thread" + num);
    }

    public static void main(String[] args) {
        try {
            File input_file = new File("input.txt");
            Scanner scan = new Scanner(input_file);
            StringBuilder data = new StringBuilder();
            //read data from file and put in string.
            while (scan.hasNextLine()) {
                data.append(scan.nextLine());
            }
            //split words in text and remove dots
            String text = data.toString();
            String[] arr_of_words = text.split(" ");

            for (int i = 0; i < arr_of_words.length; i++) {
                arr_of_words[i] = arr_of_words[i].toLowerCase(Locale.ROOT);
                String temp = arr_of_words[i];
                String word_without_dot = "";
                for (int j = 0; j < temp.length(); j++) {

                    if (temp.charAt(j) == '.' || temp.charAt(j) == ',' || temp.charAt(j) == '\"') {
                        continue;
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
            String[] arr_of_search_words = words.split(" ");
            System.out.println("********************************************");
            new MyThread("One", text1, arr_of_search_words);
            new MyThread("Two", text2, arr_of_search_words);
            new MyThread("Three", text3, arr_of_search_words);
            new MyThread("Four", text4, arr_of_search_words);

        } catch (FileNotFoundException e) {
            System.out.println("Error in open file!!");
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