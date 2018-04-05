package fantasy_football.controller;

import fantasy_football.exceptions.UnauthenticatedUserException;
import fantasy_football.model.PlayerTeam;
import fantasy_football.model.User;
import fantasy_football.service.UserService;
import fantasy_football.service.FantasyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nfl")
public class Controller {

    @Autowired
    FantasyService fantasyService;

    @Autowired
    UserService userService;

    /**
     * FantasyFootball: GET request that runs a SELECT query (Read)
     *
     * @param id integer to specify object id
     * @return the PlayerTeam object with that id
     */
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public PlayerTeam getById(@RequestParam(value = "api") String apiKey,
                              @RequestParam(value = "id", defaultValue = "1") int id) throws UnauthenticatedUserException {
        if(userService.verifyAPI(apiKey)){
        return fantasyService.getByID(id);
    }
        else {
            throw new UnauthenticatedUserException("That is not a valid API Key");
        }
    }

    /**
     * Users: GET request that runs a SELECT query (Read)
     *
     * @param email User's email address (*
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/user")
    public User getByEmail(@RequestParam(value = "email") String email) {
        return userService.getByEmail(email);
    }

    /**
     * Users:  POST request runs an INSERT query (Create)
     *
     * @param user User object. Must contain a unique email at a minimum
     * @return the User object if successful
     */
    @RequestMapping(method = RequestMethod.POST, value = "/user")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * Users: PATCH request runs an UPDATE query (Update)
     *
     * @param user User object. Must contain a unique email at a minimum
     * @return the User object if successful
     */
    @RequestMapping(method = RequestMethod.PATCH, value = "/user")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * Users: POST request to request a unique API key for the user.
     *
     * @param user User object. Must contain a unique email at a minimum
     * @return the User object with the new API key, or with their existing
     *      API key if they already had one.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/api")
    public User requestApiKey(@RequestBody User user) {
        return userService.requestApiKey(user);
    }

}