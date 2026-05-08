package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.Rule;

import java.util.Set;

public interface AccidentRuleRepository {

    Rule add(Rule rule);

    boolean delete(Integer id);

    Set<Rule> findAll();

    Set<Rule> findAllByIds(Set<Integer> ruleIds);

    /* Нужно для удобства тестирования */
    default void clearRepository() {
        var accidentTypes = findAll();
        for (var oneAccidentType : accidentTypes) {
            delete(oneAccidentType.getId());
        }
    }
}
