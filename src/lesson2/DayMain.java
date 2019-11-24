package lesson2;


public class DayMain {
    public static int maxWorkingDays = 5;

    public enum DayOfWeek {    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY  }

    public static String getWorkingHours(DayOfWeek day){
        int numberOfDay = day.ordinal();
        int amountWorkingDays = maxWorkingDays - numberOfDay;
        int workingHours = amountWorkingDays * 8;

        if (workingHours > 0){
            return "кол-во рабочих часов до конца недели составляет " + workingHours + " часов!";
        } else {
            return "cегодня выходной день!";
        }
    }

    public static void main(final String[] args) {
        System.out.println(getWorkingHours(DayOfWeek.MONDAY));
        System.out.println();
        System.out.println(getWorkingHours(DayOfWeek.SUNDAY));

    }
}
