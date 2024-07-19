public class NumberWorker{
    public boolean isPrime(int number) {
        if (number <= 1 )
            throw IllegalNumberxception();
        for (int i = 2; i < Math.sqrt(number) ;i++)
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