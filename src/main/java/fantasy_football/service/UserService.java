package fantasy_football.service;

import fantasy_football.exceptions.APIGenerationException;
import fantasy_football.exceptions.DatabaseException;
import fantasy_football.mapper.UserMapper;
import fantasy_football.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    /**
     * Returns one record from the database by their email address
     *
     * @param email String identifying the user by their unique
     *              email address
     * @return User object of the entry
     */
    public User getByEmail(String email) throws DatabaseException {
        try {
            return userMapper.getByEmail(email);
        } catch (Exception ex) {
            throw new DatabaseException("Unable to retrieve User from the database");
        }
    }

    /**
     * Adds individual entry in the database
     *
     * @param user User object with the information to add into
     *             the database.
     * @return User object of the new entry by their email
     * @throws DatabaseException if user was not added to the
     *                           database (addUser() returned 0 rows updated)
     */

    public User addUser(User user) throws DatabaseException {
        try {
            if (userMapper.addUser(user) > 0) {
                return userMapper.getByEmail(user.getEmail());
            } else {
                throw new DatabaseException("User was not added");
            }
        } catch (DuplicateKeyException e) {
            throw new DatabaseException("User already exists in the database, cannot add duplicate");
        } catch (Exception e) {
            throw new DatabaseException("User could not be added");
        }
    }

    /**
     * Updates individual entry in the database
     *
     * @param user User object with the information to update
     *             the database.
     * @return User object of the updated entry by their email
     * @throws DatabaseException if user was not updated in the
     *                           database (updateUser() returned 0 rows updated)
     */
    public User updateUser(User user) throws DatabaseException {
        if (userMapper.updateUser(user) > 0) {
            return userMapper.getByEmail(user.getEmail());
        } else {
            throw new DatabaseException("User was not updated");
        }
    }


    /**
     * If the user does not exist in the database, first add them.
     * Once the user exists in the database, check if they already have
     * an API key. If they do, return a User object with their existing
     * API key. If not, call generateKey() and update their database
     * record with their API key.
     * <p>
     * Return the user, as per their information in the database.
     *
     * @param user User object requesting an API key
     * @return User object that includes their new API key
     * @throws DatabaseException if any/all of the database calls were
     *                           unsuccessful
     */
    public User requestApiKey(User user) throws DatabaseException, APIGenerationException {
        if (userMapper.getByEmail(user.getEmail()) == null) {
            addUser(user);
        }
        if (userMapper.getByEmail(user.getEmail()).getApiKey() == null) {
            user.setApiKey(generateKey());
            updateUser(user);
        }
        return userMapper.getByEmail(user.getEmail());
    }

    /**
     * Generates a unique API key using Java's KeyGenerator and SecretKey.
     * <p>
     * Uses Advanced Encryption Standard (AES) with a 128 bit key (although
     * you could modify it to be 192 or 256 if need be)
     *
     * @return String of a unique API Key
     */
    public static String generateKey() throws APIGenerationException {

        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new APIGenerationException("API was unable to be generated");
        }
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        byte[] encoded = secretKey.getEncoded();
        return DatatypeConverter.printHexBinary(encoded).toLowerCase();
    }

    /**
     * Checks if an API key exists in the database.
     *
     * @param apiKey String of a unique API Key
     * @return boolean true if the key exists
     */
    public boolean verifyAPI(String apiKey) throws DatabaseException {
        try {
            if (userMapper.verifyKey(apiKey) == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            throw new DatabaseException("Unable to verify API key in database");
        }
    }
}
