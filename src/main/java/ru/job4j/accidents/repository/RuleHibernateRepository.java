package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;

import java.util.*;

@Repository
@AllArgsConstructor
public class RuleHibernateRepository implements AccidentRuleRepository {

    private final HbnCrudRepository hbnCrudRepository;

    @Override
    public Rule add(Rule rule) {
        hbnCrudRepository.run(session -> session.save(rule));
        return rule;
    }

    @Override
    public boolean delete(Integer id) {
        int updatedLines = hbnCrudRepository.run(
                "DELETE Rule WHERE id = :id",
                Map.of("id", id)
        );
        return updatedLines > 0;
    }

    @Override
    public Set<Rule> findAll() {
        String query = "FROM Rule AS r ORDER BY r.id ASC";
        return new HashSet<>(hbnCrudRepository.query(query, Rule.class));
    }

    @Override
    public Set<Rule> findAllByIds(Set<Integer> ruleIds) {
        Set<Rule> resultSet = new HashSet<>();
        if (ruleIds != null && !ruleIds.isEmpty()) {
            resultSet = new HashSet<>(hbnCrudRepository.query("FROM Rule AS r WHERE r.id IN (:ids)", Rule.class, Map.of("ids", ruleIds)));
        }
        return resultSet;
    }
}
