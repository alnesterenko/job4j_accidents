package ru.job4j.accidents.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.AccidentRuleRepository;

import java.util.Set;

@ThreadSafe
@Service
public class SimpleAccidentRuleService implements AccidentRuleService {

    private final AccidentRuleRepository ruleRepository;

    public SimpleAccidentRuleService(AccidentRuleRepository ruleMem) {
        this.ruleRepository = ruleMem;
    }

    @Override
    public Set<Rule> findAll() {
        return ruleRepository.findAll();
    }

    @Override
    public Set<Rule> findAllByIds(Set<Integer> ruleIds) {
        return ruleRepository.findAllByIds(ruleIds);
    }
}
