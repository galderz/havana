package j.text;

import java.util.Arrays;

public class Split
{
    public static void main(String[] args)
    {
        {
            String str = "geekss@for@geekss";

            // Applies limit-1 times, so in this case 1
            System.out.println(Arrays.toString(str.split("@", 2)));
            System.out.println(Arrays.toString(str.split("@", 5)));
            System.out.println(Arrays.toString(str.split("@", -2)));

            System.out.println(Arrays.toString(str.split("s", 2)));
            System.out.println(Arrays.toString(str.split("s", 5)));
            System.out.println(Arrays.toString(str.split("s", -2)));

            // Trailing empty removed
            System.out.println(Arrays.toString(str.split("s", 0)));
        }

        {
            String str = "GeeksforGeeks:A Computer Science Portal";
            System.out.println(Arrays.toString(str.split(":")));
        }

        {
            String str = "GeeksforGeeksforStudents";
            System.out.println(Arrays.toString(str.split("for")));
        }

        {
            String str = "Geeks for Geeks";
            System.out.println(Arrays.toString(str.split(" ")));
        }

        {
            String str = "Geekssss";
            System.out.println(Arrays.toString(str.split("s")));
        }

        {
            String str = "GeeksforforGeeksfor   ";
            System.out.println(Arrays.toString(str.split("for")));
        }

        {
            String str = "word1, word2 word3@word4?word5.word6";
            System.out.println(Arrays.toString(str.split("[, ?.@]+")));
        }

        {
            String str = "google.com:8080";
            System.out.println(Arrays.toString(str.split("[:]+")));
        }

        {
            String str = "http://google.com:8080";
            System.out.println(Arrays.toString(str.split("[://]+")));
        }

        {
            String str = "http://google.com:8080/a";
            System.out.println(Arrays.toString(str.split("[://]+")));
        }

        {
            String str = "http://google.com:8080/a/b/c";
            System.out.println(Arrays.toString(str.split("[://]+")));
        }
    }

}
