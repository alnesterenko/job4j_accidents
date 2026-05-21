package ru.job4j.accidents.service;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleAccidentTypeService implements AccidentTypeService {

    private final AccidentTypeRepository typeRepository;

    public SimpleAccidentTypeService(AccidentTypeRepository accidentTypeHibernateRepository) {
        this.typeRepository = accidentTypeHibernateRepository;
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
