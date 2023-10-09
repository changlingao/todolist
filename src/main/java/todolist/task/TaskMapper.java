package todolist.task;

import org.apache.ibatis.annotations.*;
import todolist.task.Task;

import java.util.List;

@Mapper
public interface TaskMapper {
    @Select("SELECT * FROM tasks")
    List<Task> findAll();

    @Insert("INSERT INTO tasks (username, description, is_completed) VALUES (#{username}, #{description}, #{isCompleted})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Task task);

    @Delete("DELETE FROM tasks WHERE id = #{id}")
    void deleteTask(Long id);

    @Select("SELECT * FROM tasks WHERE username = #{username}")
    List<Task> findByUsername(String username);

}
