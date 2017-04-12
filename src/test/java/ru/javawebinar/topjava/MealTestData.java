package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ+2;
    public static final Meal MEAL_1=new Meal(START_SEQ+2,LocalDateTime.parse("2017-04-07T09:00"), "Завтрак", 800);
    public static final Meal MEAL_2=new Meal(START_SEQ+3,LocalDateTime.parse("2017-04-07T14:00"), "Обед", 1000);
    public static final Meal MEAL_3=new Meal(START_SEQ+4,LocalDateTime.parse("2017-04-07T19:00"), "Ужин", 700);
    public static final Meal MEAL_4=new Meal(START_SEQ+5,LocalDateTime.parse("2017-04-08T09:00"), "Завтрак", 850);
    public static final Meal MEAL_5=new Meal(START_SEQ+6,LocalDateTime.parse("2017-04-08T14:00"), "Обед", 1200);
    public static final Meal MEAL_6=new Meal(START_SEQ+7,LocalDateTime.parse("2017-04-08T18:30"), "Ужин", 800);
    public static final Meal MEAL_7=new Meal(START_SEQ+8,LocalDateTime.parse("2017-04-09T09:30"), "Завтрак", 800);
    public static final Meal MEAL_8=new Meal(START_SEQ+9,LocalDateTime.parse("2017-04-09T15:10"), "Обед", 1000);
    public static final Meal MEAL_9=new Meal(START_SEQ+10,LocalDateTime.parse("2017-04-09T19:00"), "Ужин", 200);

    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.parse("2017-04-07T09:00"), "Завтрак", 800),
            new Meal(LocalDateTime.parse("2017-04-07T14:00"), "Обед", 1000),
            new Meal(LocalDateTime.parse("2017-04-07T19:00"), "Ужин", 700),
            new Meal(LocalDateTime.parse("2017-04-08T09:00"), "Завтрак", 850),
            new Meal(LocalDateTime.parse("2017-04-08T14:00"), "Обед", 1200),
            new Meal(LocalDateTime.parse("2017-04-08T18:30"), "Ужин", 800),
            new Meal(LocalDateTime.parse("2017-04-09T09:30"), "Завтрак", 800),
            new Meal(LocalDateTime.parse("2017-04-09T15:10"), "Обед", 1000),
            new Meal(LocalDateTime.parse("2017-04-09T19:00"), "Ужин", 200)
    );

//    public static final ModelMatcher<Meal> MATCHER = new ModelMatcher<>(
//            (expected, actual) -> expected == actual ||
//                    (expected.toString().equals(actual.toString()))
//
//    );

}
