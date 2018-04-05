package fantasy_football.controller;

import fantasy_football.exceptions.*;
import fantasy_football.model.GeneralResponse;
import fantasy_football.model.PlayerTeam;
import fantasy_football.model.User;
import fantasy_football.service.UserService;
import fantasy_football.service.FantasyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
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
    public GeneralResponse getById(@RequestParam(value = "api") String apiKey,
                                   @RequestParam(value = "id", defaultValue = "1") int id) throws UnauthenticatedUserException, DatabaseException {
        if (userService.verifyAPI(apiKey)) {
            GeneralResponse gr = new GeneralResponse();
            gr.setData(fantasyService.getByID(id));
            return gr;
        } else {
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
    public GeneralResponse getByEmail(@RequestParam(value = "email") String email) throws DatabaseException {
        User user = null;
        try {
            user = userService.getByEmail(email);
            if (user != null) {
                GeneralResponse gr = new GeneralResponse();
                gr.setData(user);
                return gr;
            } else {
                throw new DatabaseException("User does not exist in the database.");
            }
        } catch (DatabaseException e) {
            throw e;
        }
    }

    /**
     * Users:  POST request runs an INSERT query (Create)
     *
     * @param user User object. Must contain a unique email at a minimum
     * @return the User object if successful
     * @throws DatabaseException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/user")
    public GeneralResponse addUser(@RequestBody User user) throws DatabaseException {
        try {
            user = userService.addUser(user);
            GeneralResponse gr = new GeneralResponse();
            gr.setData(user);
            return gr;
        } catch (DatabaseException e) {
            throw e;
        }
    }

    /**
     * Users: PATCH request runs an UPDATE query (Update)
     *
     * @param user User object. Must contain a unique email at a minimum
     * @return the User object if successful
     * @throws DatabaseException
     */
    @RequestMapping(method = RequestMethod.PATCH, value = "/user")
    public GeneralResponse updateUser(@RequestBody User user) throws DatabaseException {
        try {
            user = userService.updateUser(user);
            GeneralResponse gr = new GeneralResponse();
            gr.setData(user);
            return gr;
        } catch (DatabaseException e) {
            throw e;
        }
    }

    /**
     * Users: POST request to request a unique API key for the user.
     *
     * @param user User object. Must contain a unique email at a minimum
     * @return the User object with the new API key, or with their existing
     * API key if they already had one.
     * @throws DatabaseException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/api")
    public GeneralResponse requestApiKey(@RequestBody User user) throws DatabaseException, APIGenerationException {
        try {
            user = userService.requestApiKey(user);
            GeneralResponse gr = new GeneralResponse();
            gr.setData(user);
            return gr;
        } catch (DatabaseException e) {
            throw e;
        }
    }
}