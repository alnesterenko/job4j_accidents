package ru.job4j.accidents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.service.AccidentService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AccidentController {

    private final AccidentService accidents;

    private final List<AccidentType> accidentTypeList;

    public AccidentController(AccidentService simpleAccidentService) {
        this.accidents = simpleAccidentService;
        this.accidentTypeList = createAccidentTypeList();
    }

    @GetMapping("/createAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("pageTitle", "Создание инцидента");
        model.addAttribute("types", accidentTypeList);
        return "accident/createAccident";
    }

    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident) {
        setCorrectType(accident);
        accidents.add(accident);
        return "redirect:/index";
    }

    @GetMapping("/formUpdateAccident")
    public String update(@RequestParam("id") int id, Model model) {
        var accidentOptional = accidents.findById(id);
        if (accidentOptional.isEmpty()) {
            model.addAttribute("message", "Инцидент с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("pageTitle", "Редактирование инцидента");
        model.addAttribute("accident", accidentOptional.get());
        model.addAttribute("types", accidentTypeList);
        return "accident/editAccident";
    }

    @PostMapping("/updateAccident")
    public String update(@ModelAttribute Accident accident) {
        setCorrectType(accident);
        accidents.update(accident.getId(), accident);
        return "redirect:/";
    }

    private List<AccidentType> createAccidentTypeList() {
        List<AccidentType> types = new ArrayList<>();
        types.add(new AccidentType(1, "Две машины"));
        types.add(new AccidentType(2, "Машина и человек"));
        types.add(new AccidentType(3, "Машина и велосипед"));
        types.add(new AccidentType(4, "Два велосипеда"));
        return types;
    }

    private void setCorrectType(Accident accident) {
        int typeId = accident.getType().getId();
        accident.setType(accidentTypeList.get(typeId - 1));
    }
}