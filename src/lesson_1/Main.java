package lesson_1;
import lesson_1.Competitors.*;
import lesson_1.Obstracle.*;

public class Main {
    public static void main(String[] args) {
        Team team1 = new Team("champion", new Human("Mister_X"), new Dog("Sharik"), new Cat("Barsik"), new Dog("Malish"));
        team1.infoTaem();
        Course course1 = new Course(new Cross(500), new Wall(5), new Water(15));
        course1.doIt(team1);
        team1.showResults();
    }
}
