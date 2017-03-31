package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GKislin
 * 15.09.2015.
 */
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach((meal) -> save(meal,1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if(!checkUser(meal,userId)) return meal;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        LOG.info("save " + meal);
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (get(id, userId) != null) {
            LOG.info("delete meal with id= " + id);
            repository.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        if (checkUser(meal, userId)) {
            LOG.info("get meal with id= " + id);
            return meal;
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll() {
        List<Meal> values = (List<Meal>) repository.values();
        if (values.isEmpty()) return Collections.emptyList();
        Collections.sort(values, (m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()));
        return values;
    }

    private boolean checkUser(Meal meal, int userId) {
        return meal.getUser().getId() == userId;
    }
}


