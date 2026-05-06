package ru.job4j.accidents.service;

import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Optional;

public interface AccidentService {

    Accident add(Accident accident);

    boolean update(Integer id, Accident accident);

    boolean delete(Integer id);

    List<Accident> findAll();

    Optional<Accident> findById(Integer id);
}
