package fantasy_football.mapper;

import fantasy_football.model.PlayerTeam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FantasyMapper {

    String GET_BY_ID = ("SELECT p.id, p.f_name, p.l_name, p.position, t.location, t.name as team_name " +
            "FROM players p JOIN players_teams pt ON p.id = pt.player_id " +
            "JOIN teams t ON t.id = pt.team_id " +
            "WHERE p.id = #{id}");

    @Select(GET_BY_ID)
    public PlayerTeam getByID(int id);
}
