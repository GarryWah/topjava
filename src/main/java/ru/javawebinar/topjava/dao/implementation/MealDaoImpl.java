package ru.javawebinar.topjava.dao.implementation;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Admin on 3/27/2017.
 */
public final class MealDaoImpl implements MealDao {
    private static final Map map = new ConcurrentHashMap<Integer, Meal>();
    private static AtomicInteger staticId=new AtomicInteger(0);


    public MealDaoImpl() {
        init();

    }

    @Override
    public void createOrUpdate(Integer id, LocalDateTime localDateTime, String description, int calories) {
        if (id == null) {
            id = staticId.incrementAndGet();
        }
        map.put(id, new Meal(id, localDateTime, description, calories));
    }

    @Override
    public void delete(Integer id) {
        map.remove(id);

    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Meal getById(Integer id) {
        return (Meal) map.get(id);
    }

    private void init() {

        createOrUpdate(null, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        createOrUpdate(null, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        createOrUpdate(null, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
        createOrUpdate(null, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
        createOrUpdate(null, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
        createOrUpdate(null, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);

    }

}
