package br.com.beehome.backTaskManager.dto;

import java.time.LocalDateTime;

import br.com.beehome.backTaskManager.model.enums.StatusEnum;
import br.com.beehome.backTaskManager.model.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskDTO(
        Long id,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull StatusEnum status,
        @NotNull LocalDateTime deadline,
        LocalDateTime createdOn,
        @NotNull Long assignedTo
) {
    public TaskDTO(Task task){
        this(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDeadline(),
                task.getCreatedOn(),
                task.getAssignedTo());
    }
}
