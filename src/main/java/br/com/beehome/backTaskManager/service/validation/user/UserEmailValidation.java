package br.com.beehome.backTaskManager.service.validation.user;

import br.com.beehome.backTaskManager.dto.UserRegisterDTO;
import br.com.beehome.backTaskManager.exception.CustomizeException;
import br.com.beehome.backTaskManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEmailValidation implements UserValidation {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(UserRegisterDTO userRegisterDTO) {
        if(userRepository.findByEmail(userRegisterDTO.email()).isPresent()){
            throw new CustomizeException("Email já cadastrado na base de dados");
        }
    }
}
