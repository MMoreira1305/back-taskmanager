package br.com.beehome.backTaskManager.service.validation.task;

import br.com.beehome.backTaskManager.dto.TaskDTO;

public interface ValidationTask {
    void validate(TaskDTO data);
}