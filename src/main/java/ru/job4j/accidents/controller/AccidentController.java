package ru.job4j.accidents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

@Controller
public class AccidentController {

    private final AccidentService accidents;

    public AccidentController(AccidentService simpleAccidentService) {
        this.accidents = simpleAccidentService;
    }

    @GetMapping("/createAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("pageTitle", "Создание инцидента");
        return "accident/createAccident";
    }

    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident, Model model) {
        if (!validate(accident)) {
            model.addAttribute("message", "Все поля должны быть заполнены!");
            return "accident/createAccident";
        }
        accidents.add(accident);
        return "redirect:/index";
    }

    private boolean validate(Accident accident) {
        boolean valid = true;
        var name = accident.getName();
        var text = accident.getText();
        var address = accident.getAddress();
        if ((name == null || name.isBlank())
                || (text == null || text.isBlank())
                || (address == null || address.isBlank())) {
            valid = false;
        }
        return valid;
    }
}