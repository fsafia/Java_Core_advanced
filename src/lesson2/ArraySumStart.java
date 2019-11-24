package lesson2;

import java.util.ArrayList;

public class ArraySumStart {
    public static void main(String[] args) {
        String [][] b = {{"1","2","3","a"},{"1","2","3","a"},{"1","2","3","4"},{"1","2","3","4"}};
        String [][] a = {{"1","2"},{"3","4"}};
        ArSum array = new ArSum();
        try {
            array.sumArray(a);
            System.out.println("Сумма элементов массива " + array.sum);


        }
        catch (MyArrayDataException e){
            System.out.println(e.getMessage());
        }
        catch (MyArraySizeException e){
            System.out.println(e.getMessage());

        }

    }
}
