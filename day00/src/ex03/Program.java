package ex03;
import java.util.Scanner;

//When you call nextInt(), nextDouble(), etc., it reads the input but does not consume the newline character (\n) at the end of the line.
// Therefore, the next call to nextLine() consumes the leftover newline character and immediately returns an empty string.
public class Program {
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String inputWeek = scanner.nextLine();
        int index = 1;
        int grade = 0;
        while (index <= 18 && !inputWeek.equals("42"))
        {
            if (!inputWeek.equals("Week " + index))
                System.exit(illegalArgument());
            grade = packGrade(getMinGrade(scanner), grade, index);
            index++;
            System.out.print("grade "+ grade + " ");
            inputWeek = scanner.nextLine();
        }
//        System.out.print("graaaade "+ grade + " ");
//        System.out.print("index "+ index + " ");
        for (int i = 1; i < index; i++) {
            draw(breakDown(i, grade), i);
        }
    }

    private static int breakDown(int index, int grade) {
        int ret;
        for (int i = 1; i < index; i++)
        {
            grade /= 10;
        }
        ret  = grade % 10;
        return ret;
    }
    private static void draw(int index, int a) {
        System.out.print("Week "+ a + " ");
        for (int i = 0; i < index; i++)
        {
            System.out.print("=");
        }
        System.out.println(">");
    }

    private static int packGrade(int minGrade, int grade, int index) {
        int ret;
        int powTen = 1;
        for (int i = 1; i < index; i++)
        {
            powTen *= 10;
        }
        ret = grade + (minGrade * powTen);
        return ret;
    }
    private static int getMinGrade(Scanner scanner) {
        int min = scanner.nextInt();
        int current;
        int i = 0;

        while (i < 4)
        {
            current = scanner.nextInt();
            min = (min < current) ? min : current;
            i++;
        }
        scanner.nextLine();
        return (min);
    }
    private static int illegalArgument() {
        System.err.print("Illegal Argument");
        return (-1);
    }
}
