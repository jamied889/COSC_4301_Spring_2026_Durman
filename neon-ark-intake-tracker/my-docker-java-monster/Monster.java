public class Monster {
    private String name;
    private String type;

    public Monster(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getDescription() {
        return name + " is a " + type + "-type monster from the Neon Ark training program.";
    }
}