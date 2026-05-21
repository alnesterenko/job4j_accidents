package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccidentTypeHibernateRepository implements AccidentTypeRepository {

    private final HbnCrudRepository hbnCrudRepository;

    @Override
    public AccidentType add(AccidentType accidentType) {
        hbnCrudRepository.run(session -> session.save(accidentType));
        return accidentType;
    }

    @Override
    public boolean delete(Integer id) {
        int updatedLines = hbnCrudRepository.run(
                "DELETE AccidentType WHERE id = :id",
                Map.of("id", id)
        );
        return updatedLines > 0;
    }

    @Override
    public List<AccidentType> findAll() {
        String query = "FROM AccidentType AS at ORDER BY at.id ASC";
        return hbnCrudRepository.query(query, AccidentType.class);
    }

    @Override
    public Optional<AccidentType> findById(Integer id) {
        String query = "FROM AccidentType AS at WHERE at.id = :id";
        return hbnCrudRepository.optional(query, AccidentType.class, Map.of("id", id));
    }
}
