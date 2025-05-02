package br.com.beehome.backTaskManager.repository;

import br.com.beehome.backTaskManager.model.enums.StatusEnum;
import br.com.beehome.backTaskManager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(nativeQuery = true,
            value = """
                    select * from task
                    where status = :status
                    and assigned_to = :user_system
                    """)
    List<Task> findByStatus(@Param("status") String status, @Param("user_system") Long user_system);

    @Query(nativeQuery = true,
            value = """
                    select * from task
                    where title = :title
                    and assigned_to = :user_system
                    """)
    Optional<Task> findByTitle(@Param("title") String title, @Param("user_system") Long user_system);

    @Query(nativeQuery = true,
            value = """
                    select * from task
                    where assigned_to = :user_system
                    """)
    List<Task> findAll(@Param("user_system") Long user_system);
}
