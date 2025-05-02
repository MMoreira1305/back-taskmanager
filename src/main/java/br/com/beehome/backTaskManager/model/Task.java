package br.com.beehome.backTaskManager.model;

import br.com.beehome.backTaskManager.dto.TaskDTO;
import br.com.beehome.backTaskManager.dto.TaskUpdateDTO;
import br.com.beehome.backTaskManager.model.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
public class Task {
    public Task(TaskDTO dto) {
        this.id = dto.id();
        this.title = dto.title();
        this.description = dto.description();
        this.status = dto.status();
        this.deadline = dto.deadline();
        this.createdOn = LocalDateTime.now();
        this.assignedTo = dto.assignedTo();
    }

    public Task(TaskUpdateDTO dto) {
        this.id = dto.id();
        this.title = dto.title();
        this.description = dto.description();
        this.status = dto.status();
        this.deadline = dto.deadline();
        this.createdOn = dto.createdOn();
        this.assignedTo = dto.assignedTo();
    }

    public Task(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Identificador único.

    @Column(name = "title", nullable = false, length = 80)
    private String title; //Título da tarefa.

    @Column(name = "description", nullable = false, length = 80)
    private String description; // Descrição da tarefa.

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 80)
    private StatusEnum status; //Enum (PENDING, IN_PROGRESS, COMPLETED).

    @Column(name = "created_on", nullable = false, length = 80)
    private LocalDateTime createdOn; //Data de criação.

    @Column(name = "deadline", nullable = false, length = 80)
    private LocalDateTime deadline; //Data limite para conclusão.

    @Column(name = "assigned_to", nullable = false, length = 80)
    private Long assignedTo; //Relacionamento com User (usuário responsável).

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

}
