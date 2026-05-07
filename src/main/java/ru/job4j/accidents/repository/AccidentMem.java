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
public class AccidentMem implements AccidentRepository {

    private final AccidentTypeRepository typeRepository;

    private int nextId = 1;

    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    public AccidentMem(AccidentTypeRepository accidentTypeMem) {
        this.typeRepository = accidentTypeMem;
        add(new Accident(0,
                "Столкновение велосипедистов",
                "Два велосипедиста столкнулись лбами на велосипедной дорожке. Оба барана думали, что другой ОБЯЗАН уступить дорогу.",
                "Велосипедная дорожка",
                typeRepository.findById(4).orElse(null)));
        add(new Accident(0,
                "Потеря груза",
                "Осенизатор разлил большую часть своего \"драгоценного\" груза",
                "На въезде в посёлок",
                typeRepository.findById(2).orElse(null)));
        add(new Accident(0,
                "Пробитие бачка",
                "Ярик, во время гасания по сельской дороге, пробил бачок",
                "Сельская дорога",
                typeRepository.findById(2).orElse(null)));
    }

    @Override
    public Accident add(Accident accident) {
        accident.setId(nextId++);
        accidents.put(accident.getId(), accident);
        return accident;
    }

    @Override
    public boolean update(Integer id, Accident accident) {
        return accidents.computeIfPresent(
                id,
                (key, value) -> new Accident(
                        id,
                        accident.getName(),
                        accident.getText(),
                        accident.getAddress(),
                        accident.getType())) != null;
    }

    @Override
    public boolean delete(Integer id) {
        return accidents.remove(id) != null;
    }

    @Override
    public List<Accident> findAll() {
        return new ArrayList<>(accidents.values());
    }

    @Override
    public Optional<Accident> findById(Integer id) {
        return Optional.ofNullable(accidents.get(id));
    }
}
