import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner; // Import the Scanner class to read text files


public class Main {


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
                arr_of_words[i]= arr_of_words[i].toLowerCase(Locale.ROOT);
                String temp = arr_of_words[i];
                String word_without_dot = "";
                for (int j = 0; j < temp.length(); j++) {

                    if (temp.charAt(j)=='.'|| temp.charAt(j)==',' || temp.charAt(j)=='\"' ){
                        continue;
                    }else {
                        word_without_dot+=temp.charAt(j);
                    }
                }
                arr_of_words[i] = word_without_dot;
            }
            int number_of_words = arr_of_words.length;
            System.out.println(Arrays.toString(arr_of_words));
            System.out.println(arr_of_words.length);
            String text1 = "", text2 = "", text3 = "", text4 = "";
            for (int i = 0; i < arr_of_words.length/4; i++) {
                text1 += arr_of_words[i];
                text1 += " ";
            }
            for (int i = arr_of_words.length/4; i < 2*(arr_of_words.length/4); i++) {
                text2 += arr_of_words[i];
                text2 += " ";
            }
            for (int i = 2*(arr_of_words.length/4); i < 3*(arr_of_words.length/4); i++) {
                text3 += arr_of_words[i];
                text3 += " ";
            }
            for (int i = 3*(arr_of_words.length/4); i < arr_of_words.length; i++) {
                text4 += arr_of_words[i];
                text4 += " ";
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error in open file!!");
            e.printStackTrace();
        }
    }

}
