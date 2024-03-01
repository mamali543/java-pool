package ex01;

import java.util.Scanner;
//Static methods can call other static methods within the same class directly without needing an instance of the class.

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final int input = scanner.nextInt();
        int exit_code = 0;
        boolean isPrime = true;
        int steps = 0;
        int sqrt;
        if (input <= 1) {
            exit_code = illegalArg();
        }
        else if (input == 2) {
            System.out.println(isPrime + " " + 1);
        }
        else
        {
            //Static methods can call other static methods within the same class directly without needing an instance of the class.
            sqrt = sqrt(input);
            System.out.println(sqrt);
            for (int i = 2; i <= sqrt + 1; i++)
            {
                steps++;
                if (input % i == 0) {
                    isPrime = false;
                    break;
                }
            }
            System.out.println(isPrime + " " + steps);
        }
        System.exit(exit_code);
    }
    //default (no modifier): If no access modifier is specified, the member has default accessibility,
    //which means it is accessible only within the same package.
    static int sqrt(int n) {
        long start = 1;
        long end = n;
        long ret = 0;
        long mid = 0;
        while (start <= end)
        {
            mid = (start + end) / 2;
            if (mid * mid == n)
                return (int) mid;
            else if (mid * mid < n) {
                start = mid + 1;
                ret = mid;
            } else
                end = mid - 1;
        }
        return (int) ret;
    }
    static int illegalArg() {
        System.err.println("IllegalArgument");
        return (-1);
    }
}
