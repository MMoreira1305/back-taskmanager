package br.com.beehome.backTaskManager.dto;

import br.com.beehome.backTaskManager.model.enums.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskUpdateDTO(Long id,
                            @NotBlank String title,
                            @NotBlank String description,
                            @NotNull StatusEnum status,
                            @NotNull LocalDateTime deadline,
                            @NotNull LocalDateTime createdOn,
                            @NotNull Long assignedTo) {
}
