package fantasy_football.model;

public class Team {

    private int teamID;
    private String location;
    private String teamName;

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return getLocation() + " " + getTeamName() + " (id = " + getTeamID() + ")";
    }
}
