package fantasy_football.service;

import fantasy_football.exceptions.DatabaseException;
import fantasy_football.mapper.FantasyMapper;
import fantasy_football.model.GeneralResponse;
import fantasy_football.model.Player;
import fantasy_football.model.PlayerTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FantasyService {

    @Autowired
    FantasyMapper fantasyMapper;

    /**
     * Returns one record from the database by their id number
     *
     * @param id integer of object to be returned
     * @return DigitalCurrencyDaily object of the entry by id
     */
    public PlayerTeam getByID(int id) throws DatabaseException {
        PlayerTeam pt = null;
        try {
            pt = fantasyMapper.getByID(id);
            if (pt != null) {
                return pt;
            } else {
                throw new DatabaseException("ID does not match any players in the database.");
            }
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
