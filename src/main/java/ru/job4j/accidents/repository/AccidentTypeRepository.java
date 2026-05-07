package ru.job4j.accidents.repository;

import ru.job4j.accidents.model.AccidentType;

import java.util.List;
import java.util.Optional;

public interface AccidentTypeRepository {

    AccidentType add(AccidentType accidentType);

    boolean delete(Integer id);

    List<AccidentType> findAll();

    Optional<AccidentType> findById(Integer id);

    /* Нужно для удобства тестирования */
    default void clearRepository() {
        var accidentTypes = findAll();
        for (var oneAccidentType : accidentTypes) {
            delete(oneAccidentType.getId());
        }
    }
}
