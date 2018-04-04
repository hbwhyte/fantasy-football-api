package fantasy_football.service;

import fantasy_football.mapper.FantasyMapper;
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
    public PlayerTeam getByID(int id) {
        return fantasyMapper.getByID(id);
    }
}
