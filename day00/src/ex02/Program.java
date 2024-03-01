package ex02;

import java.util.Scanner;

//default (no modifier): If no access modifier is specified, the member has default accessibility,
//which means it is accessible only within the same package.
public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        int coffee_request = 0;

        while (input != 42)
        {
            if (isPrime(sumOfDigit(input)))
                coffee_request++;
            input = scanner.nextInt();
        }
        System.out.println("Count of coffee-request : " + coffee_request);
    }
    static int sumOfDigit(int input) {
        int a = 0;
        while (input != 0)
        {
            a += input % 10;
            input /= 10;
        }
        return a;
    }

    static boolean isPrime(int input) {
        int sqrt = sqrt(input);

        if (input <= 1)
            return false;
        else if (input == 2)
            return true;
        else
        {
            for (int i = 2; i <= sqrt; i++)
            {
                if (input % i == 0)
                    return false;
            }
        }
        return true;
    }

    static int sqrt(int n) {
        long start = 1;
        long end = n;
        long ret = 0;
        long mid;

        while (start <= end)
        {
            mid = (start + end) / 2;
            if (mid * mid == n)
                return (int)mid;
            else if (mid * mid < n) {
                start = mid + 1;
                ret = mid;
            }
            else
                end = mid - 1;
        }
        return (int) ret;
    }
}
