import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import mr.school1337.numbers.NumberWorker;

public class NumberWorkerTest {

    private NumberWorker numberWorker = new NumberWorker();

    // Test for prime numbers
    @ParameterizedTest
    @ValueSource(ints = {2, 3, 5, 7, 11})
    public void isPrimeForPrimes(int number) {
        Assertions.assertTrue(numberWorker.isPrime(number));
    }

    // Test for non-prime numbers
    @ParameterizedTest
    @ValueSource(ints = {4, 6, 8, 9, 10})
    public void isPrimeForNotPrimes(int number) {
        Assertions.assertFalse(numberWorker.isPrime(number));
    }

    // Test for incorrect numbers (0, 1, negative numbers)
    @ParameterizedTest
    @ValueSource(ints = {0, 1, -1, -5, -10})
    public void isPrimeForIncorrectNumbers(int number) {
        Assertions.assertThrows(IllegalNumberException.class, () -> numberWorker.isPrime(number));
    }

    // Test digitsSum using data from CSV file
    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv")
    public void digitsSum(int number, int expectedSum) {
        int actualSum = numberWorker.digitsSum(number);
        Assertions.assertEquals(expectedSum, actualSum);
    }
}
