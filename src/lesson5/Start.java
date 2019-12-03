package lesson5;

public class Start {
    public static void main(String[] args) {
        ArrayConversion arrayConversion = new ArrayConversion(1);
        arrayConversion.arrayChangeInOneThread();

        ArrayConversion arrayConversion2 = new ArrayConversion(2);
        arrayConversion2.arrayChangeByMultipleThread();

    }
}
