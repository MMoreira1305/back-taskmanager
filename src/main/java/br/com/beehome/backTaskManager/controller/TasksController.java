package br.com.beehome.backTaskManager.controller;

import br.com.beehome.backTaskManager.dto.TaskDTO;
import br.com.beehome.backTaskManager.dto.TaskUpdateDTO;
import br.com.beehome.backTaskManager.exception.CustomizeException;
import br.com.beehome.backTaskManager.infra.security.SecurityConfig;
import br.com.beehome.backTaskManager.model.enums.StatusEnum;
import br.com.beehome.backTaskManager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = SecurityConfig.SECURITY)
@Tag(name = "tasks", description = "Controlador para receber requisições de tasks")
public class TasksController {

    @Autowired
    private TaskService service;

    @GetMapping
    @Operation(summary = "Busca uma lista de dados de task", description = "Busca dados de task a partir do usuário logado")
    @ApiResponse(responseCode = "200", description = "Lista de Tasks retornada")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<List<TaskDTO>> get(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca dados de task", description = "Busca dados de task a partir de um ID e usuário logado")
    @ApiResponse(responseCode = "200", description = "Task retornada")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<TaskDTO> getById(@PathVariable Long id){
        Optional<TaskDTO> taskDTO = service.getById(id);
        return ResponseEntity.ok(service.getById(id).orElseThrow(
                () -> new EntityNotFoundException("Tarefa não encontrada"))
        );
    }

    @Transactional(rollbackOn = {Exception.class, RuntimeException.class, CustomizeException.class})
    @PostMapping
    @Operation(summary = "Salva os dados da task", description = "Utiliza validações de title, description, data de término e etc")
    @ApiResponse(responseCode = "200", description = "Task criada")
    @ApiResponse(responseCode = "400", description = "Algum campo obrigatório errado ou faltando")
    public ResponseEntity<TaskDTO> post(@Valid @RequestBody TaskDTO taskDTO){
        TaskDTO taskSaved = service.saveTask(taskDTO);
        return ResponseEntity.ok(taskSaved);
    }

    @Transactional(rollbackOn = {Exception.class, RuntimeException.class, CustomizeException.class})
    @PutMapping("/{id}")
    @Operation(summary = "Altera os dados da task", description = "Utiliza validações de title, description, data de término e etc")
    @ApiResponse(responseCode = "400", description = "Algum campo obrigatório errado ou faltando")
    public ResponseEntity<TaskDTO> put(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO taskUpdateDTO){
        if(service.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.updateTask(id, taskUpdateDTO));
    }

    @Operation(summary = "Deletar uma task", description = "Deleta as informações da task")
    @ApiResponse(responseCode = "201", description = "Task deletada com sucesso")
    @Transactional(rollbackOn = {Exception.class, RuntimeException.class, CustomizeException.class})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    @Operation(summary = "Busca dados de task a partir de filtro", description = "Busca dados de task a partir de filtro e status")
    @ApiResponse(responseCode = "200", description = "Lista de Tasks retornada")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<List<TaskDTO>> getByFilter(@RequestParam(value = "status", defaultValue = "PENDING") StatusEnum status){
        try{
            return ResponseEntity.ok(service.getByStatus(status.name()));
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomizeException(e.getMessage());
        }

    }

}
