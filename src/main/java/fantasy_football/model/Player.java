package fantasy_football.model;

public class Player {

    private int playerID;
    private String firstName;
    private String lastName;
    private String position;

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + ", " +
                getPosition() + " (id = " + getPlayerID() + ")";
    }
}
