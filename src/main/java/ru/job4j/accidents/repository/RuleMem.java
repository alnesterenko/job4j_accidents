package ru.job4j.accidents.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ThreadSafe
@Repository
public class RuleMem implements AccidentRuleRepository {

    private AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Rule> accidentRules = new ConcurrentHashMap<>();

    public RuleMem() {
        add(new Rule(1, "Нарушение законов логики"));
        add(new Rule(2, "Нарушение законов физики"));
        add(new Rule(3, "Нарушение законов здравого смысла"));
        add(new Rule(4, "Камикадзе на дороге"));
        add(new Rule(5, "Незаконная попытка вызвать чувство кринжа"));
        add(new Rule(6, "Испанский стыд"));
    }

    @Override
    public Rule add(Rule rule) {
        rule.setId(nextId.getAndIncrement());
        accidentRules.put(rule.getId(), rule);
        return rule;
    }

    @Override
    public boolean delete(Integer id) {
        return accidentRules.remove(id) != null;
    }

    @Override
    public Set<Rule> findAll() {
        return new HashSet<>(accidentRules.values());
    }

    @Override
    public Set<Rule> findAllByIds(Set<Integer> ruleIds) {
        return ruleIds.stream()
                .map(accidentRules::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
