package br.com.beehome.backTaskManager.dto;

import br.com.beehome.backTaskManager.model.User;

public record UserDTO(
        Long id,
        String username,
        String email,
        String password
) {
    public UserDTO(User user){
        this(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }
}
