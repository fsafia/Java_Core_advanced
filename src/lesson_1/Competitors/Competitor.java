package lesson_1.Competitors;

public interface Competitor {
    void run(int dist);
    void swim(int dist);
    void jump(int height);
    boolean isOnDistance();
    void info();
    String getName();
}