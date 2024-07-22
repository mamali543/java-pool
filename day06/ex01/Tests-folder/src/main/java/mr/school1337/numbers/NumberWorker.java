package mr.school1337.numbers;
import mr.school1337.numbers.IllegalNumberException;

public class NumberWorker{
    public boolean isPrime(int number) {
        if (number <= 1 )
            throw new IllegalNumberException();
        for (int i = 2; i <= Math.sqrt(number) ;i++)
        {
            if (number % i == 0)
                return false;
        }
        return true;
    }

    public int digitsSum(int number) {
        int a = 0;
        number = Math.abs(number);
        while (number > 0)
        {
            a+= number%10;
            number /= 10;
        }
        return a;
    }
}