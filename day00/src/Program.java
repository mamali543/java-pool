//when you try to execute java Program, you encounter an error because the JVM expects the class to be in the default package
public class Program {
    public static void main(String[] args) { //main method entry point for the program
        int number = 543799;
        int result =0;
        for (int i = 0; i < 5; i++)
        {
            result += number % 10;
            number /= 10;
        }
        result += number%10;
        System.out.println(result);
    }
}
