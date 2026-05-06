package ru.job4j.accidents.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@ThreadSafe
@Controller
public class IndexController {
    @GetMapping({"/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("pageTitle", "Начальная страница");
        model.addAttribute("user", "Petr Arsentev");
        return "index";
    }
}
