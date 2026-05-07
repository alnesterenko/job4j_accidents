package ru.job4j.accidents.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
@Repository
public class AccidentTypeMem implements AccidentTypeRepository {

    private int nextId = 1;

    private final Map<Integer, AccidentType> accidentTypes = new ConcurrentHashMap<>();

    public AccidentTypeMem() {
        add(new AccidentType(0, "Две машины"));
        add(new AccidentType(0, "Машина и человек"));
        add(new AccidentType(0, "Машина и велосипед"));
        add(new AccidentType(0, "Два велосипеда"));
        add(new AccidentType(0, "Танк и вертолёт"));
        add(new AccidentType(0, "Восемь мотоциклов"));
    }

    @Override
    public AccidentType add(AccidentType accidentType) {
        accidentType.setId(nextId++);
        accidentTypes.put(accidentType.getId(), accidentType);
        return accidentType;
    }

    @Override
    public boolean delete(Integer id) {
        return accidentTypes.remove(id) != null;
    }

    @Override
    public List<AccidentType> findAll() {
        return new ArrayList<>(accidentTypes.values());
    }

    @Override
    public Optional<AccidentType> findById(Integer id) {
        return Optional.ofNullable(accidentTypes.get(id));
    }
}
