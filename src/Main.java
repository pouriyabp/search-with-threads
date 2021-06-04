import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
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
            System.out.println(data);
        } catch (FileNotFoundException e) {
            System.out.println("Error in open file!!");
            e.printStackTrace();
        }
    }

}
