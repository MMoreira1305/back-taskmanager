package br.com.beehome.backTaskManager.service;

import br.com.beehome.backTaskManager.dto.TaskDTO;
import br.com.beehome.backTaskManager.dto.TaskUpdateDTO;
import br.com.beehome.backTaskManager.exception.CustomizeException;
import br.com.beehome.backTaskManager.model.enums.StatusEnum;
import br.com.beehome.backTaskManager.model.Task;
import br.com.beehome.backTaskManager.model.User;
import br.com.beehome.backTaskManager.repository.TaskRepository;
import br.com.beehome.backTaskManager.service.validation.task.ValidationTask;
import br.com.beehome.backTaskManager.utils.CurrentUserService;
import br.com.beehome.backTaskManager.utils.DTOConverter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class TaskService {

    private static final Logger logger = Logger.getLogger(TaskService.class.getName());

    @Autowired
    private List<ValidationTask> validations;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private DTOConverter dtoConverter;

    @Autowired
    private TaskRepository repository;

    public List<TaskDTO> getAll() {
        try{
            User currentUser = currentUserService.getCurrentUser();
            return Optional.of(repository.findAll(currentUser.getId()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(TaskDTO::new)
                    .toList();
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomizeException("Erro ao buscar dados: " + e.getMessage());
        }

    }

    public Optional<TaskDTO> getById(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada"));

        User currentUser = currentUserService.getCurrentUser();
        if (!task.getAssignedTo().equals(currentUser.getId())) {
            throw new AccessDeniedException("Você não tem permissão para acessar esta tarefa.");
        }
        return Optional.of(new TaskDTO(task));
    }

    public TaskDTO saveTask(TaskDTO taskDTO) {
        validateTask(taskDTO);
        try{
            return new TaskDTO(repository.save(new Task(taskDTO)));
        }catch (Exception e){
            logger.warning(e.getMessage());
            throw new RuntimeException("Erro ao salvar tarefa, favor tentar novamente mais tarde!");
        }

    }

    public TaskDTO updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        if(getById(id).isEmpty()) throw new EntityNotFoundException("Task não foi localizada");

        StatusEnum statusEnum = taskUpdateDTO.status();
        if(taskUpdateDTO.deadline().isBefore(LocalDateTime.now()) && !statusEnum.name().equals("COMPLETED"))
            throw new CustomizeException("Data de término da tarefa não pode ser menor que dia de hoje!");

        try {
            return new TaskDTO(repository.save(new Task(taskUpdateDTO)));
        }catch (Exception e){
            logger.warning(e.getMessage());
            throw new RuntimeException("Erro ao alterar tarefa, favor tentar novamente mais tarde!");
        }
    }

    public void deleteById(Long id){
        if(getById(id).isEmpty()) throw new EntityNotFoundException("Task não foi localizada");

        try {
            repository.deleteById(id);
        }catch (Exception e){
            logger.warning(e.getMessage());
            throw new RuntimeException("Erro ao deletar task, favor tentar mais tarde!");
        }
    }

    public List<TaskDTO> getByStatus(String status) {
        User currentUser = currentUserService.getCurrentUser();
        return Optional.ofNullable(repository.findByStatus(status, currentUser.getId()))
                .orElse(Collections.emptyList())
                .stream()
                .map(TaskDTO::new)
                .toList();
    }

    private void validateTask(TaskDTO taskDTO) {
        validations.forEach(v -> v.validate(taskDTO));
    }
}
