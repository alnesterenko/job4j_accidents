package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Optional;

public interface OldAccidentRepository {

    Accident add(Accident accident);

    boolean update(Integer id, Accident accident);

    boolean delete(Integer id);

    List<Accident> findAll();

    Optional<Accident> findById(Integer id);

    /* Нужно для удобства тестирования */
    default void clearRepository() {
        var accidents = findAll();
        for (var oneAccident : accidents) {
            delete(oneAccident.getId());
        }
    }
}
