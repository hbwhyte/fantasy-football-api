package fantasy_football.service;

import fantasy_football.exceptions.DatabaseException;
import fantasy_football.mapper.UserMapper;
import fantasy_football.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;

@Service
public class AuthService {

    @Autowired
    UserMapper userMapper;

    public static String generateKey(){

        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        byte[] encoded = secretKey.getEncoded();
        return DatatypeConverter.printHexBinary(encoded).toLowerCase();
    }

    public User getByEmail(String email) {
        return userMapper.getByEmail(email);
    }

    public User requestApiKey(User user) {
        if (userMapper.getByEmail(user.getEmail()) == null) {
            addUser(user);
        }
        if (userMapper.getByEmail(user.getEmail()).getApiKey() == null) {
            user.setApiKey(generateKey());
            updateUser(user);
            user.setId(userMapper.getByEmail(user.getEmail()).getId());
        }
        return user;
    }

    public User addUser(User user) {
        if (userMapper.addUser(user) > 0) {
            return userMapper.getByEmail(user.getEmail());
        } else {
            throw new DatabaseException("User was not added");
        }
    }

    public User updateUser(User user) {
        if (userMapper.updateUser(user) > 0) {
            return userMapper.getByEmail(user.getEmail());
        } else {
            throw new DatabaseException("User was not updated");
        }
    }
}
