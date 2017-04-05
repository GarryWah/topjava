package ru.javawebinar.topjava.repository.mock;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Table<Integer,Integer,Meal> repository= HashBasedTable.create();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach((meal) -> save(meal, 1));
    }

    @Override
    public Meal  save(Meal meal, int userId) {

        if (!checkUser(meal, userId)) return meal;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        LOG.info("save " + meal);
        synchronized (repository) {
            repository.put(userId, meal.getId(), meal);
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (get(id, userId) != null) {
            LOG.info("delete meal with id= " + id);
            synchronized (repository) {
                repository.remove(userId,id);
            }
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal;
        synchronized (repository) {
            meal = repository.get(userId,id);
        }
        if (meal != null && checkUser(meal, userId)) {
            LOG.info("get meal with id= " + id);
            return meal;
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {

        List<Meal> values;
        synchronized (repository) {
            values = new ArrayList<>(repository.row(userId).values());
        }
        if (values.isEmpty()) return Collections.emptyList();
        Collections.sort(values, (m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()));
        return values;
    }

    @Override
    public Collection<Meal> getFilteredByDate(LocalDate startDate, LocalDate endDate,int userId) {
        return getAll(userId).stream().filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate)).collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getFilteredByTime(LocalTime startTime, LocalTime endTime, int userId) {
        return getAll(userId).stream().filter(meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)).collect(Collectors.toList());
    }

    private boolean checkUser(Meal meal, int userId) {
        return meal.getUserId() == userId;
    }

}


