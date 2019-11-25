package lesson3;


public class Start {
    public static void main(String[] args) {

        ArrayOfWords arrayOfWords = new ArrayOfWords("Антилопа", "белка", "верблюд", "гусь", "Дятел", "енот", "жираф", "Заяц",
                "Индюк", "попугай", "Попугай","попугай","попугай", "белка", "белка", "Дятел", "заяц", "заяц", "заяц", "заяц");

        arrayOfWords.wordCount();

        System.out.println("2 задание");

        PhoneBook phoneBook = new PhoneBook();

        phoneBook.add("h", "211");
        phoneBook.add("h", "111");
        phoneBook.add("d", "111");
        phoneBook.add("d", "111");
        phoneBook.getPhoneNumber("h");
        phoneBook.getPhoneNumber("d");
        phoneBook.getPhoneNumber("s");
    }
}
