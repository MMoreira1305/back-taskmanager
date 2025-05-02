package br.com.beehome.backTaskManager.utils;

import br.com.beehome.backTaskManager.model.User;
import br.com.beehome.backTaskManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserService {
    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var usuario = (User) authentication.getPrincipal();
        return userRepository.findByEmail(usuario.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
