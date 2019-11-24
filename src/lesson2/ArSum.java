package lesson2;

public class ArSum {

    public int sum = 0;

    public int sumArray(String[][] array) throws MyArraySizeException, MyArrayDataException{

        if (! (array.length == 4))
            throw new MyArraySizeException("Неверный размер массива");

        for (int i = 0; i < array.length; i++) {
            int col = array[i].length;
            if(!(col == 4)){ throw new MyArraySizeException("Неверный размер массива");}
        }


        for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array[i].length ; j++) {
                try {
                    sum += Integer.parseInt(array[i][j]);
                }
                catch (NumberFormatException e){
                    throw new MyArrayDataException("В ячейке [" +  i + "] " + "[" + j+ "] " + " лежат неверные данные!");
                }
            }

        }
        return sum;
    }
}
