package ru.job4j.accidents.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentTypeRepository;

import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleAccidentTypeService implements AccidentTypeService {

    private final AccidentTypeRepository typeRepository;

    public SimpleAccidentTypeService(AccidentTypeRepository accidentTypeMem) {
        this.typeRepository = accidentTypeMem;
    }

    @Override
    public List<AccidentType> findAll() {
        return typeRepository.findAll();
    }

    @Override
    public Optional<AccidentType> findById(Integer id) {
        return typeRepository.findById(id);
    }
}
