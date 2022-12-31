import java.util.Scanner;

public class Palindrom {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Input your word: ");
        String word = in.nextLine();
        String[] words = word.split(" ");
        in.close();
        for (int i = 0; i < words.length; i++) {
            String str = words[i].toLowerCase();
            System.out.println(isPalindrome(str));
        }
    }

    public static String reverseLine(String normalLine) {
        char[] arrayLine = normalLine.toCharArray();
        String reversedLine = "";
        for (int i = arrayLine.length - 1; i >= 0; i--) {
            reversedLine = reversedLine + arrayLine[i];
        }
        return reversedLine;
    }

    public static boolean isPalindrome(String normalLine) {
        String reversedLine = reverseLine(normalLine);
        if (normalLine.equals(reversedLine)) {
            return true;
        } else {
            return false;
        }
    }

}
