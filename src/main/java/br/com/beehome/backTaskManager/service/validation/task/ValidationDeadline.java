package br.com.beehome.backTaskManager.service.validation.task;

import br.com.beehome.backTaskManager.dto.TaskDTO;
import br.com.beehome.backTaskManager.exception.CustomizeException;
import br.com.beehome.backTaskManager.model.enums.StatusEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidationDeadline implements ValidationTask{
    @Override
    public void validate(TaskDTO data) {
        StatusEnum statusEnum = data.status();
        if(data.deadline().isBefore(LocalDateTime.now()) && !statusEnum.name().equals("COMPLETED"))
            throw new CustomizeException("Data de término da tarefa não pode ser menor que dia de hoje!");
    }
}
