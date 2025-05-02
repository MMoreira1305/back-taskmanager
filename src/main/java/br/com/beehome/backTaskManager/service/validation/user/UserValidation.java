package br.com.beehome.backTaskManager.service.validation.user;

import br.com.beehome.backTaskManager.dto.UserRegisterDTO;

public interface UserValidation {
    void validate(UserRegisterDTO userRegisterDTO);
}
