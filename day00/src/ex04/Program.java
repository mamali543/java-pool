package ex04;
import java.util.Arrays;
import java.util.Scanner;


/*In Java, characters are represented as integers using their Unicode code points*/

/*When you try to print an array directly, it doesn't print the contents of the array,
but rather the memory address where the array is stored, which is why you see [C@240237d2.
Instead, you should use Arrays.toString() to print the contents of the array*/

public class Program {

    private static final int MAX_CHAR_CODES = 65535;
    private static final int MAX_TOP_CHARS = 10;
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        short[] charactersCount = getCharOccurrences(input);
        char[] topTenChars = getTopTenChars(charactersCount);
        printGraph(topTenChars, charactersCount);
    }

    private static short[] getCharOccurrences(String input) {

        short[] ret = new short[MAX_CHAR_CODES];
        //convert String to an array of characters
        char[] parsedInput = input.toCharArray();
        //The expression ret[parsedInput[i]]++ uses the ASCII value of the character as an index into the ret array.
        for (int i =0; i < parsedInput.length; i++)
        {
            ret[parsedInput[i]]++;
        }
        return ret;
    }

    private static char[] getTopTenChars(short[] charactersCount) {
        char[] ret = new char[MAX_TOP_CHARS];
        //loop over the 65535 case,and checks if a case holds a value greater than 0,
        for (int i =0; i < MAX_CHAR_CODES; i++)
        {
            int charCount = charactersCount[i];
            if (charCount > 0)
            {
                for (int j = 0; j < MAX_TOP_CHARS; j++)
                {
                    if (charactersCount[ret[j]] < charCount)
                    {
                        ret = insertCharAt(ret, j, (char) i);
                        System.out.println("ret: "+Arrays.toString(ret));
                        break;
                    }
                }
            }
        }
        return ret;
    }

    private static char[] insertCharAt(char[] ret, int j, char i) {
        char[] ret1 = new char[MAX_TOP_CHARS];
        for (int k = 0; k < j;k++)
        {
            ret1[k] = ret[k];
        }
        ret1[j] = i;
        for (int k = j+1; k < MAX_TOP_CHARS;k++)
        {
            ret1[k] = ret[k-1];
        }
        return ret1;
    }
    
    private static void printGraph(char[] topTenChars, short[] charactersCount) {
        short max = charactersCount[topTenChars[0]];
        short maxHeight = max < 10 ? max : 10;
        short totalLines = (short)(maxHeight + 2);
        short[] graphs = new short[MAX_TOP_CHARS];

        for (int i = 0; i < MAX_TOP_CHARS; i++)
        {
            if (max <= 10)
            {
                graphs[i] = charactersCount[topTenChars[i]];
            }
            else
            {
                graphs[i] = (short)(charactersCount[topTenChars[i]] * 10 / max);
            }
        }
        System.out.println();
        for (int i = 0; i < totalLines; i++) {
            for (int j = 0; j < MAX_TOP_CHARS; j++) {
                if (topTenChars[j] != 0) {
                    //verify if the # number + 2 + i === total lines ,then print the characterCount of the top ten character,
                    //else if we are on the last line i === totalLines -1 print the character
                    //
                    if (i + graphs[j] + 2 == totalLines) {
                        System.out.printf("%3d", charactersCount[topTenChars[j]]);
                    } else if (i == totalLines - 1) {
                        System.out.printf("%3c", topTenChars[j]);
                    } else if (i + graphs[j] >= maxHeight) {
                        System.out.printf("%3c", '#');
                    }
                }
            }
            System.out.println();
        }
    }
}
