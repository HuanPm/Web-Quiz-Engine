package engine.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/register")
    public void register(@Valid @RequestBody WebUser webUser) {
        if (userRepository.findById(webUser.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The email has already existed.");
        }

        this.userRepository.save(webUser);

        UserDetails userDetails = User.builder()
                .username(webUser.getEmail())
                .password(passwordEncoder.encode(webUser.getPassword()))
                .roles("WEBUSER")
                .build();
        this.inMemoryUserDetailsManager.createUser(userDetails);
    }

}
