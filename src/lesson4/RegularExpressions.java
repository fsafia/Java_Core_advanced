package lesson4;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressions {

    public String inputPassword(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();
        return password;
    }

    public boolean isValidPassword(String password){
        boolean result = false;

        Pattern pattern = Pattern.compile("^.*\\s.*"); // 5 правило наличие пробельного символа
        Matcher matcher = pattern.matcher(password);
        result = matcher.matches();

        if (result == true){return false;}

        List<Pattern> patterns = new ArrayList<>();

        patterns.add(Pattern.compile("^.{8,20}$"));//1 правило колличество символов
        patterns.add(Pattern.compile("^.*\\d.*$"));//2 правило наличие цифры
        patterns.add(Pattern.compile("^.*[a-z].*$"));// 3 наличие строчной буквы
        patterns.add(Pattern.compile("^.*[A-Z].*$"));// 4 наличие заглавной буквы

        for (Pattern p: patterns) {
            Matcher m = p.matcher(password);

            result = m.matches();
            if(result == false){
                return false;
            }
        }
        return result;

    }
}
