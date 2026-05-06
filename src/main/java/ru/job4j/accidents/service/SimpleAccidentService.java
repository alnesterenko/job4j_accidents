package ru.job4j.accidents.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.AccidentRepository;

import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleAccidentService implements AccidentService {

    private final AccidentRepository accidentRepository;

    public SimpleAccidentService(AccidentRepository accidentMem) {
        this.accidentRepository = accidentMem;
    }

    @Override
    public Accident add(Accident accident) {
        return accidentRepository.add(accident);
    }

    @Override
    public boolean update(Integer id, Accident accident) {
        return accidentRepository.update(id, accident);
    }

    @Override
    public boolean delete(Integer id) {
        return accidentRepository.delete(id);
    }

    @Override
    public List<Accident> findAll() {
        return accidentRepository.findAll();
    }

    @Override
    public Optional<Accident> findById(Integer id) {
        return accidentRepository.findById(id);
    }
}
