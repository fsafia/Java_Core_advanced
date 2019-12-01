package lesson4;

public class Test {
    public static void main(String[] args) {
        RegularExpressions re = new RegularExpressions();
        String password = re.inputPassword();
        System.out.println("Валиден ли введенный пароль : " + re.isValidPassword(password));

    }

}
