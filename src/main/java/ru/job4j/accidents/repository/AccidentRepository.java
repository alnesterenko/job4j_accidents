package ru.job4j.accidents.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.Accident;

import java.util.Optional;

public interface AccidentRepository extends CrudRepository<Accident, Integer> {

    @EntityGraph(attributePaths = {"type", "rules"})
    Iterable<Accident> findAll();

    @EntityGraph(attributePaths = {"type", "rules"})
    Optional<Accident> findById(Integer id);
}