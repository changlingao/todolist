package todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import todolist.task.Task;
import todolist.task.TaskMapper;


import java.util.List;

@Controller
public class TaskController {
    @Autowired
    private TaskMapper taskMapper;

    private Long userid = null;

    @GetMapping("/dashboard/{userid}")
    public String index(Model model, @PathVariable Long userid) {
        this.userid = userid;
        List<Task> tasks = taskMapper.findByUserid(userid);
        model.addAttribute("tasks", tasks);
        model.addAttribute("userid", userid);
        return "dashboard"; // Render the HTML page named "dashboard.html"
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task) {
        task.setUserid(this.userid);
        taskMapper.insert(task);
        return "redirect:/dashboard";
    }

    @PostMapping("/deleteTask/{taskId}")
    public String deleteTask(@PathVariable Long taskId) {
        taskMapper.deleteTask(taskId);
        return "redirect:/dashboard";
    }





}