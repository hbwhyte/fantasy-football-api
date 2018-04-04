package fantasy_football.mapper;

import fantasy_football.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    String GET_BY_ID = ("SELECT * FROM FantasyDraft.users WHERE email = #{email}");

    String UPDATE_USER = ("UPDATE FantasyDraft.users " +
            "SET firstName=#{firstName}, lastName=#{lastName}, email=#{email}, apiKey=#{apiKey} " +
            "WHERE email=#{email}");

    String ADD_USER = ("INSERT INTO FantasyDraft.users " +
            "(firstName, lastName, email, apiKey) VALUES" +
            "(#{firstName}, #{lastName}, #{email}, #{apiKey})");

    @Select(GET_BY_ID)
    public User getByEmail(String email);

    @Update(UPDATE_USER)
    public int updateUser(User user);

    @Insert(ADD_USER)
    public int addUser(User user);
}
