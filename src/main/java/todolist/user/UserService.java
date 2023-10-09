package todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void registerUser(String username, String password) {
        User existingUser = userMapper.findByUsername(username);
        if (existingUser == null) {
            User user = new User(username, password);
            userMapper.insert(user);
        } else {
            throw new RuntimeException("Username already exists");
        }
    }

    public Long authenticate(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null) {
            // Compare the provided password with the stored hashed password
            if (password.equals(user.getPassword())) {
                return user.getId();
            }
        }
        return (long)-1;
    }
}

