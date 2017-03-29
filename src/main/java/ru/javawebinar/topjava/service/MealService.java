package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Admin on 3/27/2017.
 */
public interface MealService {
    void createOrUpdate(Integer id, LocalDateTime localDateTime, String description, int calories);
    void delete(Integer id);
    List<Meal> getAll();
    Meal getById(Integer id);
}
