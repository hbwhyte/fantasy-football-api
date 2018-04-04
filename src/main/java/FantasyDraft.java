import fantasy_football.model.Player;
import fantasy_football.model.PlayerTeam;
import fantasy_football.model.Team;

import java.sql.*;
import java.util.ArrayList;

public class FantasyDraft {

    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public static void main(String[] args) throws Exception {
        FantasyDraft fantasy = new FantasyDraft();
        fantasy.getTeamList();
        fantasy.getPlayerList();

        // Optional add/delete methods
        //fantasy.addTeam("Seattle","Seahawks");
        //fantasy.deleteTeam(32); // by team ID or team name
        //fantasy.addPlayer("Doug","Baldwin", "Wide Receiver");
        //fantasy.deletePlayer(13); // by player ID or first and last name
        //fantasy.matchPlayer(10,22); // Matches Dan Bailey to the Denver Broncos

        // fantasy_football.model.Team draft of only matched players
        fantasy.draftTeam();
    }

    // Print out list of current teams in DB
    private void getTeamList() throws Exception {
        Connection connection = createConnection();
        statement = connection.createStatement();

        resultSet = statement.executeQuery("select * from teams ;");
            ArrayList<Team> teams = mapTeamsToObjects(resultSet);

            System.out.println("\nALL TEAMS");
            for (Team t : teams) {
                System.out.println(t.toString());
            }
        closeConnection(connection);
    }
    // Print out list of current players in DB
    private void getPlayerList() throws Exception {
        Connection connection = createConnection();
        statement = connection.createStatement();
        resultSet = statement.executeQuery("select * from players ;");

        ArrayList<Player> players = mapPlayersToObjects(resultSet);

        System.out.println("\nALL PLAYERS");
        for (Player p : players) {
            System.out.println(p.toString());
        }
        closeConnection(connection);
    }

    // adds team to the DB
    private void addTeam(String location, String teamName) throws Exception {
        Connection connection = createConnection();
        statement = connection.createStatement();

        preparedStatement = connection.prepareStatement
                ("insert into teams (location, name) values (?, ?)");
        // Parameters start with 1
        preparedStatement.setString(1, location);
        preparedStatement.setString(2, teamName);
        preparedStatement.executeUpdate();
        resultSet = statement.executeQuery("select * from teams");

        ArrayList<Team> teams = mapTeamsToObjects(resultSet);

        System.out.println("\nTEAMS AFTER ADDITION");
        for (Team t : teams) {
            System.out.println(t.toString());
        }

        closeConnection(connection);
    }

    // overloaded method deletes every instance of a team by team name
    private void deleteTeam(String teamName) throws Exception {
        Connection connection = createConnection();
        statement = connection.createStatement();

        preparedStatement = connection.prepareStatement("delete from teams where name = ?;");
        preparedStatement.setString(1, teamName);
        preparedStatement.executeUpdate();
        resultSet = statement.executeQuery("select * from teams");

        ArrayList<Team> teams = mapTeamsToObjects(resultSet);

        System.out.println("\nTEAMS AFTER DELETION");
        for (Team t : teams) {
            System.out.println(t.toString());
        }

        closeConnection(connection);
    }

    // Overloaded method deletes one instance of a team by teamID number
    private void deleteTeam(int teamID) throws Exception {
        Connection connection = createConnection();
        statement = connection.createStatement();

        preparedStatement = connection.prepareStatement("delete from teams where id = ?;");
        preparedStatement.setInt(1, teamID);
        preparedStatement.executeUpdate();
        resultSet = statement.executeQuery("select * from teams");

        ArrayList<Team> teams = mapTeamsToObjects(resultSet);

        System.out.println("\nTEAMS AFTER DELETION");
        for (Team t : teams) {
            System.out.println(t.toString());
        }

        closeConnection(connection);
    }

    // adds new player to the DB
    private void addPlayer(String firstName, String lastName, String position) throws Exception {
        Connection connection = createConnection();
        statement = connection.createStatement();

        preparedStatement = connection
                .prepareStatement("insert into players (f_name, l_name, position) " +
                        "values (?, ?, ?)");
        // Parameters start with 1
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setString(3, position);
        preparedStatement.executeUpdate();
        resultSet = statement.executeQuery("select * from players");

        ArrayList<Player> players = mapPlayersToObjects(resultSet);

        System.out.println("\nPLAYERS AFTER ADDITION");
        for (Player p : players) {
            System.out.println(p.toString());
        }

        closeConnection(connection);
    }

    // Overloaded method deletes every instance of a player by first and last name
    private void deletePlayer(String firstName, String lastName) throws Exception {
        Connection connection = createConnection();
        statement = connection.createStatement();

        preparedStatement = connection.prepareStatement("delete from players where f_name = ? AND l_name = ?;");
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.executeUpdate();
        resultSet = statement.executeQuery("select * from players");

        ArrayList<Player> players = mapPlayersToObjects(resultSet);

        System.out.println("\nPLAYERS AFTER DELETION");
        for (Player p : players) {
            System.out.println(p.toString());
        }

        closeConnection(connection);
    }

    // Overloaded method deletes one instance of a player by playerID number
    private void deletePlayer(int playerID) throws Exception {
        Connection connection = createConnection();
        statement = connection.createStatement();

        preparedStatement = connection.prepareStatement("delete from players where id = ?;");
        preparedStatement.setInt(1, playerID);
        preparedStatement.executeUpdate();
        resultSet = statement.executeQuery("select * from players");

        ArrayList<Player> players = mapPlayersToObjects(resultSet);

        System.out.println("\nPLAYERS AFTER DELETION");
        for (Player p : players) {
            System.out.println(p.toString());
        }

        closeConnection(connection);
    }

    // adds team to the DB
    private void matchPlayer(int playerID, int teamID) throws Exception {
        Connection connection = createConnection();
        statement = connection.createStatement();

        preparedStatement = connection.prepareStatement
                ("insert into players_teams (player_id, team_id) values (?, ?)");
        // Parameters start with 1
        preparedStatement.setInt(1, playerID);
        preparedStatement.setInt(2, teamID);
        preparedStatement.executeUpdate();
//        resultSet = statement.executeQuery("select * from players_teams");
//
//        ArrayList<fantasy_football.model.PlayerTeam> playerteam = mapPlayersTeamsToObjects(resultSet);
//
//        System.out.println("\nMY DRAFT");
//        for (fantasy_football.model.PlayerTeam pt : playerteam) {
//            System.out.println(pt.toString());
//        }

        closeConnection(connection);
    }

    // adds team to the DB
    private void draftTeam() throws Exception {
        Connection connection = createConnection();
        statement = connection.createStatement();

        resultSet = statement.executeQuery
                ("SELECT p.f_name, p.l_name, p.position, t.location, t.name as team_name " +
                        "FROM players p " +
                        "JOIN players_teams pt " +
                        "ON p.id = pt.player_id " +
                        "JOIN teams t " +
                        "ON t.id = pt.team_id;");

        ArrayList<PlayerTeam> playerTeam = mapPlayersTeamsToObjects(resultSet);

        System.out.println("\nMY DRAFT");
        for (PlayerTeam pt : playerTeam) {
            System.out.println(pt.toString());
        }
    }

    private ArrayList<Team> mapTeamsToObjects(ResultSet resultSet) throws SQLException {

        ArrayList<Team> retList = new ArrayList();

        while (resultSet.next()) {
            Team t = new Team();
            t.setTeamID(resultSet.getInt("id"));
            t.setLocation(resultSet.getString("location"));
            t.setTeamName(resultSet.getString("name"));

            retList.add(t);
        }
        return retList;
    }

    private ArrayList<Player> mapPlayersToObjects(ResultSet resultSet) throws SQLException {

        ArrayList<Player> retList = new ArrayList();

        while (resultSet.next()) {
            Player p = new Player();
            p.setPlayerID(resultSet.getInt("id"));
            p.setFirstName(resultSet.getString("f_name"));
            p.setLastName(resultSet.getString("l_name"));
            p.setPosition(resultSet.getString("position"));
            retList.add(p);
        }
        return retList;
    }

    private ArrayList<PlayerTeam> mapPlayersTeamsToObjects(ResultSet resultSet) throws SQLException {

        ArrayList<PlayerTeam> retList = new ArrayList();

        while (resultSet.next()) {
            PlayerTeam pt = new PlayerTeam();
            pt.setFirstName(resultSet.getString("f_name"));
            pt.setLastName(resultSet.getString("l_name"));
            pt.setPosition(resultSet.getString("position"));
            pt.setLocation(resultSet.getString("location"));
            pt.setTeamName(resultSet.getString("team_name"));
            retList.add(pt);
        }
        return retList;
    }

    // Creates connection to MySQL DB, for start of each db connected method
    public Connection createConnection() throws Exception {
        Connection connection;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Setup the connection with the DB
            connection = DriverManager.getConnection("jdbc:mysql://localhost/FantasyDraft?" +
                    "user=root&password=&useSSL=false");
            return connection;
        } catch (Exception e) {
            throw e;
        }
    }

    // Closes connection, needs to be at the end of each db connected method to avoid memory leak
    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

