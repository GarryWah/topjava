package ru.javawebinar.topjava.service.implementation;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Admin on 3/27/2017.
 */
public class MealServiceImpl implements MealService {
    private MealDao mealDao;

    public MealDao getMealDao() {
        return mealDao;
    }

    public void setMealDao(MealDao mealDao) {
        this.mealDao = mealDao;
    }

    public MealServiceImpl(MealDao mealDao) {
        this.mealDao = mealDao;
    }

    @Override
    public void createOrUpdate(Integer id, LocalDateTime localDateTime, String description, int calories) {
        mealDao.createOrUpdate(id, localDateTime, description, calories);

    }


    @Override
    public void delete(Integer id) {
        mealDao.delete(id);

    }

    @Override
    public List<Meal> getAll() {
        return mealDao.getAll();
    }

    @Override
    public Meal getById(Integer id) {
        return mealDao.getById(id);
    }
}
