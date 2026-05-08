package ru.job4j.accidents.service;

import ru.job4j.accidents.model.Rule;

import java.util.Set;

public interface AccidentRuleService {

    Set<Rule> findAll();

    Set<Rule> findAllByIds(Set<Integer> ruleIds);
}
