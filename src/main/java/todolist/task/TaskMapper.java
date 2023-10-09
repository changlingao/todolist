package todolist.task;

import org.apache.ibatis.annotations.*;
import todolist.task.Task;

import java.util.List;

@Mapper
public interface TaskMapper {
    @Select("SELECT * FROM tasks")
    List<Task> findAll();

    @Insert("INSERT INTO tasks (description) VALUES (#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Task task);

    @Delete("DELETE FROM tasks WHERE id = #{id}")
    void deleteTask(Long id);

    @Select("SELECT * FROM tasks WHERE userid = #{userid}")
    List<Task> findByUserid(Long userid);

}
