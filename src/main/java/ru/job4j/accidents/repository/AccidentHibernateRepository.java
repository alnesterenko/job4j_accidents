package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
@AllArgsConstructor
public class AccidentHibernateRepository implements OldAccidentRepository {

    private final HbnCrudRepository hbnCrudRepository;

    private static Logger logger = Logger.getLogger(AccidentHibernateRepository.class.getName());

    public Accident add(Accident accident) {
        hbnCrudRepository.run(session -> session.save(accident));
        return accident;
    }

    public List<Accident> findAll() {
        String query = "SELECT DISTINCT ac FROM Accident AS ac "
                + "LEFT JOIN FETCH ac.rules "
                + "JOIN FETCH ac.type "
                + "ORDER BY ac.id ASC";
        return hbnCrudRepository.query(query, Accident.class);
    }

    @Override
    public boolean update(Integer id, Accident accident) {
        boolean result = false;
        accident.setId(id);
        try {
            hbnCrudRepository.run(session -> session.update(accident));
            result = true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw e;
        }
        return result;
    }

    @Override
    public boolean delete(Integer id) {
        int updatedLines = hbnCrudRepository.run(
                "DELETE Accident WHERE id = :id",
                Map.of("id", id)
        );
        return updatedLines > 0;
    }

    @Override
    public Optional<Accident> findById(Integer id) {
        String query = "SELECT DISTINCT ac FROM Accident AS ac "
                + "LEFT JOIN FETCH ac.rules "
                + "JOIN FETCH ac.type "
                + "WHERE ac.id = :id";
        return hbnCrudRepository.optional(query, Accident.class, Map.of("id", id));
    }
}
