package br.com.beehome.backTaskManager.dto;

import br.com.beehome.backTaskManager.model.User;

public record UserLoginDTO(Long id,
                           String username,
                           String email,
                           String token) {
    public UserLoginDTO(User user, String token){
        this(user.getId(), user.getUsername(), user.getEmail(), token);
    }
}
