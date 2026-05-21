package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.AccidentHibernateRepository;
import ru.job4j.accidents.repository.AccidentRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class SimpleAccidentService implements AccidentService {

    private final AccidentRepository accidentRepository;

    private static Logger logger = Logger.getLogger(AccidentHibernateRepository.class.getName());

    @Override
    public Accident add(Accident accident) {
        return accidentRepository.save(accident);
    }

    @Override
    public boolean update(Integer id, Accident accident) {
        accident.setId(id);
        boolean success = true;
        try {
            accidentRepository.save(accident);
        } catch (Exception e) {
            logger.info(e.getMessage());
            success = false;
        }
        return success;
    }

    @Override
    public boolean delete(Integer id) {
        boolean success = true;
        try {
            accidentRepository.deleteById(id);
        } catch (Exception e) {
            logger.info(e.getMessage());
            success = false;
        }
        return success;
    }

    @Override
    public List<Accident> findAll() {
        var iterable = accidentRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }

    @Override
    public Optional<Accident> findById(Integer id) {
        return accidentRepository.findById(id);
    }
}
