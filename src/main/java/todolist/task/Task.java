package todolist.task;

public class Task {
    private Long id;
    private String username;
    private String description;
    private boolean isCompleted;

    public Task(String username, String description) {
        // id: GeneratedKeys by mapper
        this.username = username;
        this.description = description;
        this.isCompleted = false;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}