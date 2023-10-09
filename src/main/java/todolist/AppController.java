package todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import todolist.task.Task;
import todolist.task.TaskMapper;
import todolist.user.UserService;

import java.net.URI;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

@Controller
public class AppController {
    @Autowired
    private UserService userService;

    // session management
    private final SecureRandom randomNumberGenerator = new SecureRandom();
    private final HexFormat hexFormatter = HexFormat.of();
    Map<String, String> sessions = new HashMap<>();


    @Autowired
    private TaskMapper taskMapper;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {

        if (!userService.authenticate(username, password)) {
            // Redirect under ResponseEntity<String>
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/invalid")).build();
        }

        // login successfully, session management
        // Generate the session token.
        byte[] sessionTokenBytes = new byte[16];
        randomNumberGenerator.nextBytes(sessionTokenBytes);
        String sessionToken = hexFormatter.formatHex(sessionTokenBytes);

        // Store the association of the session token with the user.
        sessions.put(sessionToken, username);

        // Create HTTP headers including the instruction for the browser to store the session token in a cookie.
        String setCookieHeaderValue = String.format("session=%s; Path=/; HttpOnly; SameSite=Strict;", sessionToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", setCookieHeaderValue);

        // indeed change all the request headers cookie session
        // extract from request headers : @CookieValue

        // Redirect to the cart page, with the session-cookie-setting headers.
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).location(URI.create("/dashboard")).build();
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        userService.registerUser(username, password);
        return "redirect:/";
    }


    @GetMapping("/dashboard")
    public String showDashboard(@CookieValue(value = "session", defaultValue = "") String sessionToken, Model model) {
        String username = sessions.get(sessionToken);
        if (username == null) {
            return "invalidUser";
        }
        List<Task> tasks = taskMapper.findByUsername(username);
        model.addAttribute("tasks", tasks);
        model.addAttribute("username", username);
        return "dashboard";
    }

    @PostMapping("/addTask")
    public String addTask(@CookieValue(value = "session", defaultValue = "") String sessionToken,
                          @ModelAttribute("description") String description) {
        String username = sessions.get(sessionToken);
        if (username == null) {
            return "invalidUser";
        }
        Task task = new Task(username, description);
        taskMapper.insert(task);
        return "redirect:/dashboard";
    }

    @PostMapping("/deleteTask/{taskId}")
    public String deleteTask(@CookieValue(value = "session", defaultValue = "") String sessionToken,
                             @PathVariable Long taskId) {
        String username = sessions.get(sessionToken);
        if (username == null) {
            return "invalidUser";
        }
        taskMapper.deleteTask(taskId);
        return "redirect:/dashboard";
    }

    // invalid user
    @GetMapping("/invalid")
    public String invalid() {
        return "invalidUser";
    }

    // log out
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name = "session", defaultValue = "") String sessionToken) {
        sessions.remove(sessionToken);
        // redirect to /, need status code 300+, cannot use OK
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();
    }
}
