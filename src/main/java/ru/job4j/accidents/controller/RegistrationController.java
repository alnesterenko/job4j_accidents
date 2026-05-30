package ru.job4j.accidents.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.AuthorityRepository;
import ru.job4j.accidents.repository.UserRepository;

@Controller
public class RegistrationController {

    private final PasswordEncoder encoder;
    private final UserRepository users;
    private final AuthorityRepository authorities;

    public RegistrationController(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.encoder = passwordEncoder;
        this.users = userRepository;
        this.authorities = authorityRepository;
    }

    @PostMapping("/registration")
    public String regSave(@ModelAttribute User user, Model model) {
        var authorityOptional = authorities.findByAuthority("ROLE_USER");
        if (authorityOptional.isEmpty()) {
            model.addAttribute("message", "Что-то пошло не так");
            return "errors/404";
        }
        user.setEnabled(true);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAuthority(authorityOptional.get());
        users.save(user);
        return "redirect:/login";
    }

    @GetMapping("/registration")
    public String regPage(Model model) {
        model.addAttribute("pageTitle", "Регистрация");
        return "registration";
    }
}