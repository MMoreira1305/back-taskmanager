package br.com.beehome.backTaskManager.service;

import br.com.beehome.backTaskManager.dto.UserDTO;
import br.com.beehome.backTaskManager.dto.UserRegisterDTO;
import br.com.beehome.backTaskManager.exception.CustomizeException;
import br.com.beehome.backTaskManager.model.User;
import br.com.beehome.backTaskManager.repository.UserRepository;
import br.com.beehome.backTaskManager.service.validation.user.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository repository;

    @Autowired
    private List<UserValidation> validationList;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO register(UserRegisterDTO userRegisterDTO){
        validationList.forEach(v -> v.validate(userRegisterDTO));
        User user = new User(userRegisterDTO);

        // Adicionando senha encriptografada
        user.setPassword(passwordEncoder.encode(userRegisterDTO.password()));

        try{
            return new UserDTO(repository.save(user));
        }catch (Exception e){
            logger.warning(e.getMessage());
            throw new RuntimeException("Erro ao salvar dados do usuário! Tente novamente mais tarde");
        }

    }

    public User getByEmail(String email) {
        if(repository.findByEmail(email).isPresent()){
            return repository.findByEmail(email).get();
        }else {
            throw new CustomizeException("Usuário não encontrado");
        }
    }
}
