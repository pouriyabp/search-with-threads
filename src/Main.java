import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner; // Import the Scanner class to read text files

class MyThread implements Runnable {
    String name;
    Thread t;
    String text;
    MyThread(String threadNumber, String text) {
        name = threadNumber;
        this.text = text;
        t = new Thread(this, name);
        t.start();
    }


    public void run() {
        System.out.println("crate thread "+name+" and text is: "+ text);
        for (int i = 5; i > 0; i--) {
            System.out.println(name + ": " + i);

        }

        System.out.println(name + " exiting.");
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
            System.out.println("********************************************");
            new MyThread("One", text1);
            new MyThread("Two", text2);
            new MyThread("Three", text3);
            new MyThread("Four", text4);

        } catch (FileNotFoundException e) {
            System.out.println("Error in open file!!");
            e.printStackTrace();
        }
    }

}
