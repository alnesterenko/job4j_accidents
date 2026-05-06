package ru.job4j.accidents.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.accidents.service.AccidentService;

@ThreadSafe
@Controller
public class IndexController {

    private final AccidentService accidentService;

    public IndexController(AccidentService simpleAccidentService) {
        this.accidentService = simpleAccidentService;
    }

    @GetMapping({"/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("pageTitle", "Начальная страница");
        model.addAttribute("user", "Petr Arsentev");
        model.addAttribute("accidents", accidentService.findAll());
        return "index";
    }
}
