package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * Created by Admin on 4/23/2017.
 */
@ActiveProfiles({Profiles.ACTIVE_DB, Profiles.DATAJPA})
public class UserServiceTestDATAJPA extends UserServiceTestGeneral {
    @Autowired
    protected MealService mealService;

    public void testGetWithMeals() {

    }
}
