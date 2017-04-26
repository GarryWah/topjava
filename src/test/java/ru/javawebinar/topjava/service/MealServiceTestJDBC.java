package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * Created by Admin on 4/22/2017.
 */
@ActiveProfiles({Profiles.HSQL_DB, Profiles.JDBC})
public class MealServiceTestJDBC extends MealServiceTestGeneral {

}
