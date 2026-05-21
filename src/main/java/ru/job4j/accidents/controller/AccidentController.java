package ru.job4j.accidents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentRuleService;
import ru.job4j.accidents.service.AccidentService;
import ru.job4j.accidents.service.AccidentTypeService;

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

    /* Начальная страница. Общий список автопроишествий. */
    @GetMapping({"/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("pageTitle", "Начальная страница");
        model.addAttribute("user", "Petr Arsentev");
        model.addAttribute("accidents", accidentService.findAll());
        return "index";
    }

    /* Создание автопроишествия */
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
                       @RequestParam(value = "rulesIds", required = false) Set<Integer> rulesIds,
                       Model model) {
        accident.setRules(ruleService.findAllByIds(rulesIds));
        try {
            accidentService.add(accident);
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

    /* Редактирование автопроишествия */
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
                         @RequestParam(value = "rulesIds", required = false) Set<Integer> rulesIds,
                         Model model) {
        try {
            accident.setRules(ruleService.findAllByIds(rulesIds));
            var isUpdated = accidentService.update(accident.getId(), accident);
            if (!isUpdated) {
                model.addAttribute("message", "Автопроишествие с указанным идентификатором не найдено !");
                return "errors/404";
            }
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

    /* Удаление автопроишествия */
    @GetMapping("/deleteAccident")
    public String delete(@RequestParam("id") int id, Model model) {
        var isDeleted = accidentService.delete(id);
        if (!isDeleted) {
            model.addAttribute("message", "Автопроишествие с указанным идентификатором не найдено !");
            return "errors/404";
        }
        return "redirect:/index";
    }
}