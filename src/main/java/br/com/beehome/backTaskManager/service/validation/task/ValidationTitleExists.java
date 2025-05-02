package br.com.beehome.backTaskManager.service.validation.task;

import br.com.beehome.backTaskManager.dto.TaskDTO;
import br.com.beehome.backTaskManager.exception.CustomizeException;
import br.com.beehome.backTaskManager.model.User;
import br.com.beehome.backTaskManager.repository.TaskRepository;
import br.com.beehome.backTaskManager.utils.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationTitleExists implements ValidationTask{
    @Autowired
    private TaskRepository repository;

    @Override
    public void validate(TaskDTO data) {

        repository.findByTitle(data.title(), data.assignedTo()).ifPresent(
                existingTask -> {
                    throw new CustomizeException("Já existe uma task com este título criada");
                }
        );
    }
}
