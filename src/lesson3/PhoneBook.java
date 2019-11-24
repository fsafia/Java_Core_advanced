package lesson3;

import java.util.*;

public class PhoneBook {
    Map<String, Set<String>> phoneBook = new HashMap<>();

    public void add(String surname, String phoneNumber){
        if(phoneBook.get(surname) == null){
            Set<String>  numbers = new HashSet<>();
            numbers.add(phoneNumber);
            phoneBook.put(surname, numbers);
        } else {
            phoneBook.get(surname).add(phoneNumber);
        }
    }

    public void getPhoneNumber(String surname){
        Set<String>  numbers = phoneBook.get(surname);
        if (numbers == null){
            System.out.println("в справочнике нет человека с фамилией " + surname);
        }else {
            System.out.println("у человека с фамилией " + surname + " номер телефона: ");
            for(String phone: numbers) {
                System.out.println(phone);
            }
        }
    }
}
