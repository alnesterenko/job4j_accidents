package ru.job4j.accidents.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class AccidentMem implements OldAccidentRepository {

    private final AccidentTypeRepository typeRepository;

    private final AccidentRuleRepository ruleRepository;

    private AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    public AccidentMem(AccidentTypeRepository accidentTypeMem,
                       AccidentRuleRepository ruleMem) {
        this.typeRepository = accidentTypeMem;
        this.ruleRepository = ruleMem;

        add(new Accident(0,
                "Столкновение велосипедистов",
                "Два велосипедиста столкнулись лбами на велосипедной дорожке. Оба барана думали, что другой ОБЯЗАН уступить дорогу.",
                "Велосипедная дорожка",
                typeRepository.findById(4).orElse(null),
                ruleRepository.findAllByIds(Set.of(1, 3))));
        add(new Accident(0,
                "Потеря груза",
                "Осенизатор разлил большую часть своего \"драгоценного\" груза",
                "На въезде в посёлок",
                typeRepository.findById(2).orElse(null),
                ruleRepository.findAllByIds(Set.of(5, 6))));
        add(new Accident(0,
                "Пробитие бачка",
                "Ярик, во время гасания по сельской дороге, пробил бачок",
                "Сельская дорога",
                typeRepository.findById(2).orElse(null),
                ruleRepository.findAllByIds(Set.of(4, 5, 6))));
    }

    @Override
    public Accident add(Accident accident) {
        accident.setId(nextId.getAndIncrement());
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
                        accident.getType(),
                        accident.getRules())) != null;
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
