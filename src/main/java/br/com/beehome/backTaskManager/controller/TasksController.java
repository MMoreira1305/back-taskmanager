package br.com.beehome.backTaskManager.controller;

import br.com.beehome.backTaskManager.dto.TaskDTO;
import br.com.beehome.backTaskManager.dto.TaskUpdateDTO;
import br.com.beehome.backTaskManager.exception.CustomizeException;
import br.com.beehome.backTaskManager.model.enums.StatusEnum;
import br.com.beehome.backTaskManager.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    @Autowired
    private TaskService service;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> get(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable Long id){
        Optional<TaskDTO> taskDTO = service.getById(id);
        return ResponseEntity.ok(service.getById(id).orElseThrow(
                () -> new EntityNotFoundException("Tarefa não encontrada"))
        );
    }

    @Transactional(rollbackOn = {Exception.class, RuntimeException.class, CustomizeException.class})
    @PostMapping
    public ResponseEntity<TaskDTO> post(@Valid @RequestBody TaskDTO taskDTO){
        TaskDTO taskSaved = service.saveTask(taskDTO);
        return ResponseEntity.ok(taskSaved);
    }

    @Transactional(rollbackOn = {Exception.class, RuntimeException.class, CustomizeException.class})
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> put(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO taskUpdateDTO){
        if(service.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.updateTask(id, taskUpdateDTO));
    }

    @Transactional(rollbackOn = {Exception.class, RuntimeException.class, CustomizeException.class})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskDTO>> getByFilter(@RequestParam(value = "status", defaultValue = "PENDING") StatusEnum status){
        try{
            return ResponseEntity.ok(service.getByStatus(status.name()));
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomizeException(e.getMessage());
        }

    }

}
