package fantasy_football.controller;

import fantasy_football.model.PlayerTeam;
import fantasy_football.service.FantasyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nfl")
public class Controller {

    @Autowired
    FantasyService fantasyService;

    /**
     * GET request that runs a SELECT query (Read)
     *
     * @param id integer to specify object id
     * @return the PlayerTeam object with that id
     */
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public PlayerTeam getById(@RequestParam(value="api") String apiKey,
                              @RequestParam(value = "id") int id) {
        return fantasyService.getByID(id);
    }

// Can I add to 2 different tables from 1 request? Probably
//    /**
//     * POST request that runs an INSERT query (Create)
//     *
//     * @param entry as DigitalCurrencyDaily object in the body of the POST request.
//     * @return the new added DigitalCurrencyDaily object
//     */
//    @RequestMapping(method = RequestMethod.POST, value = "/")
//    public DigitalCurrencyDaily addNew(@RequestBody DigitalCurrencyDaily entry) {
//        return digitalDailyService.addNew(entry);
//    }

//    /**
//     * PATCH request that runs a UPDATE query (Update)
//     *
//     * @param entry as DigitalCurrencyDaily object in the body of the PATCH request.
//     * @return the DigitalCurrencyDaily object with that id
//     */
//    @RequestMapping(method = RequestMethod.PATCH, value = "/")
//    public DigitalCurrencyDaily updateById(@RequestBody DigitalCurrencyDaily entry) {
//        return digitalDailyService.updateById(entry);
//    }
//
//    /**
//     * DELETE request that runs a UPDATE query to set as inactive (Delete)
//     *
//     * @param id integer to specify object id
//     * @return the deactivated DigitalCurrencyDaily object with that id
//     */
//    @RequestMapping(method = RequestMethod.DELETE, value = "/")
//    public DigitalCurrencyDaily deleteById(@RequestParam(value = "id") int id) {
//        return digitalDailyService.deleteByID(id);
//    }
//
}
