package ru.job4j.accidents.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.mapper.AccidentRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccidentJdbcTemplateRepository implements OldAccidentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccidentJdbcTemplateRepository(JdbcTemplate jdbc) {
        this.jdbcTemplate = jdbc;
    }

    @Override
    public Accident add(Accident accident) {
        String insertAccidentSql = """
            INSERT INTO accidents (name, text, address, type_id)
            VALUES (?, ?, ?, ?)
            """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertAccidentSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, accident.getName());
            ps.setString(2, accident.getText());
            ps.setString(3, accident.getAddress());
            ps.setInt(4, accident.getType().getId());
            return ps;
        }, keyHolder);

        var generatedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        accident.setId(generatedId);

        updateAccidentRules(accident);

        return accident;
    }

    @Override
    public boolean update(Integer id, Accident accident) {
        boolean result = false;
        String updateSql = """
            UPDATE accidents
            SET name = ?, text = ?, address = ?, type_id = ?
            WHERE id = ?
            """;
        int rows = jdbcTemplate.update(updateSql,
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getType().getId(),
                id);
        if (rows > 0) {
            /* Удаляем старые связи (обязательно, чтобы не накапливались) */
            String deleteRulesSql = "DELETE FROM accident_rule_links WHERE accident_id = ?";
            jdbcTemplate.update(deleteRulesSql, id);

            updateAccidentRules(accident);
            result = true;
        }
        return result;
    }

    @Override
    public boolean delete(Integer id) {
        /* Связи удалятся автоматически благодаря ON DELETE CASCADE */
        String deleteAccidentSql = "DELETE FROM accidents WHERE id = ?";
        return jdbcTemplate.update(deleteAccidentSql, id) > 0;
    }

    @Override
    public List<Accident> findAll() {
        /* Приходится использовать такую "партянку" чтобы в AccidentRowMapper не быдо конфликтов имён */
        String sql = """
            SELECT 
                ac.id AS id,
                ac.name AS ac_name,
                ac.text AS ac_text,
                ac.address AS ac_address,
                at.id AS type_id,
                at.name AS type_name,
                r.id AS rule_id,
                r.name AS rule_name
            FROM accidents AS ac
            LEFT JOIN accident_type AS at ON ac.type_id = at.id
            LEFT JOIN accident_rule_links AS arl ON ac.id = arl.accident_id
            LEFT JOIN rule AS r ON arl.rule_id = r.id
            ORDER BY ac.id
            """;

        Map<Integer, Accident> accidentMap = new ConcurrentHashMap<>();
        jdbcTemplate.query(sql, new AccidentRowMapper(accidentMap));
        return new ArrayList<>(accidentMap.values());
    }

    @Override
    public Optional<Accident> findById(Integer id) {
        String sql = """
            SELECT 
                ac.id AS id,
                ac.name AS ac_name,
                ac.text AS ac_text,
                ac.address AS ac_address,
                at.id AS type_id,
                at.name AS type_name,
                r.id AS rule_id,
                r.name AS rule_name
            FROM accidents AS ac
            LEFT JOIN accident_type AS at ON ac.type_id = at.id
            LEFT JOIN accident_rule_links AS arl ON ac.id = arl.accident_id
            LEFT JOIN rule AS r ON arl.rule_id = r.id
            WHERE ac.id = ?
            ORDER BY ac.id
            """;

        Map<Integer, Accident> accidentMap = new ConcurrentHashMap<>();
        jdbcTemplate.query(sql, new AccidentRowMapper(accidentMap), id);

        return accidentMap.values().stream().findFirst();
    }

    @Override
    public void clearRepository() {
        /* Просто удаляем все проишествия, связи удалятся автоматически благодаря ON DELETE CASCADE */
        jdbcTemplate.update("DELETE FROM accidents");
    }

    private void updateAccidentRules(Accident accident) {
        /* Проверка нужна, чтобы если Set<Rule> пустой, быстро "выскакивать" из метода */
        if (accident.getRules() == null || accident.getRules().isEmpty()) {
            return;
        }
        /* Собираем всё в список и делаем ТОЛЬКО ОДНУ вставку в БД */
        String insertRuleLinkSql = "INSERT INTO accident_rule_links (accident_id, rule_id) VALUES (?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Rule rule : accident.getRules()) {
            batchArgs.add(new Object[]{accident.getId(), rule.getId()});
        }
        jdbcTemplate.batchUpdate(insertRuleLinkSql, batchArgs);
    }
}