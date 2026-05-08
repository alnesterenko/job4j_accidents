package ru.job4j.accidents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentRuleService;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;

import java.util.List;
import java.util.Set;

@Controller
public class AccidentController {

    private final AccidentService accidentService;

    private final AccidentTypeService typeService;

    private final AccidentRuleService ruleService;

    public AccidentController(
            AccidentService simpleAccidentService,
            AccidentTypeService simpleAccidentTypeService,
            AccidentRuleService simpleAccidentRuleService) {
        this.accidentService = simpleAccidentService;
        this.typeService = simpleAccidentTypeService;
        this.ruleService = simpleAccidentRuleService;
    }

    @GetMapping("/createAccident")
    public String viewCreateAccident(Model model) {
        model.addAttribute("pageTitle", "Создание инцидента");
        model.addAttribute("accident", new Accident());
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("rules", ruleService.findAll());
        return "accident/createAccident";
    }

    @PostMapping("/saveAccident")
    public String save(@ModelAttribute Accident accident,
                       @RequestParam(value = "rulesIds", required = false) Set<Integer> rulesIds) {
        accident.setRules(ruleService.findAllByIds(rulesIds));
        /* Так как магия Spring не срабатывает, приходится пользоваться этим методом */
        setCorrectType(accident);
        accidentService.add(accident);
        return "redirect:/index";
    }

    @GetMapping("/formUpdateAccident")
    public String update(@RequestParam("id") int id, Model model) {
        var accidentOptional = accidentService.findById(id);
        if (accidentOptional.isEmpty()) {
            model.addAttribute("message", "Инцидент с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("pageTitle", "Редактирование инцидента");
        model.addAttribute("accident", accidentOptional.get());
        model.addAttribute("types", typeService.findAll());
        model.addAttribute("rules", ruleService.findAll());
        return "accident/editAccident";
    }

    @PostMapping("/updateAccident")
    public String update(@ModelAttribute Accident accident,
                         @RequestParam(value = "rulesIds", required = false) Set<Integer> rulesIds) {
        accident.setRules(ruleService.findAllByIds(rulesIds));
        /* Так как магия Spring не срабатывает, приходится пользоваться этим методом */
        setCorrectType(accident);
        accidentService.update(accident.getId(), accident);
        return "redirect:/";
    }

    private void setCorrectType(Accident accident) {
        int typeId = accident.getType().getId();
        accident.setType(typeService.findById(typeId).orElse(null));
    }
}