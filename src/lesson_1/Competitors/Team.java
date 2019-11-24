package lesson_1.Competitors;

public class Team {
    private String teamName;
    public Competitor []team;


    public Team(String teamName, Competitor... t) {
        this.teamName = teamName;
        this.team = t;
    }


    public void showResults(){
        for (Competitor c : team) {
            c.info();
        }
    }

    public void infoTaem(){
        System.out.println("Taem '" + teamName + "' consists:");
        for (int i = 0; i < team.length; i++) {
            System.out.println(team[i].getName() +" ");
        }

        System.out.println();

    }

}
