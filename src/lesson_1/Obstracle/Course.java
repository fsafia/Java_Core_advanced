package lesson_1.Obstracle;

import lesson_1.Competitors.Competitor;
import lesson_1.Competitors.Team;


public class Course {
    private Obstacle [] obstacles;

    public Course(Obstacle...o){
        obstacles = o;
    }

    public void doIt(Team t){
        for (Competitor c : t.team) {
            for (Obstacle o : obstacles) {
                o.doIt(c);
                if (!c.isOnDistance()) break;
            }
        }

    }
}
