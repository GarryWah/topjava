package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)

        );
        long startTime = System.nanoTime() / 1000000;
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(13, 0), 2000));
        long stopTime = System.nanoTime() / 1000000;
        System.out.println(stopTime - startTime);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field
        List<UserMealWithExceed> userMeals;
//with streams
        Map<LocalDate, Integer> sum = mealList.stream().collect(
                Collectors.groupingBy((t) -> t.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        userMeals = mealList.stream()
                .filter(userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal ->
                        sum.getOrDefault(userMeal.getDateTime().toLocalDate(), 0) > caloriesPerDay ?
                                new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true) :
                                new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false))
                .collect(Collectors.toList());

        return userMeals;
//with loops
       /* List<UserMealWithExceed> userMeals=new ArrayList<>();
        Map<LocalDate, Integer> sum = new HashMap<>();
        LocalDate localDate;
        Integer tmpSum;
        for (UserMeal userMeal : mealList) {
            localDate = userMeal.getDateTime().toLocalDate();
            tmpSum = sum.getOrDefault(localDate, 0);
            if (tmpSum != 0) {
                sum.put(localDate, tmpSum + userMeal.getCalories());
            } else {
                sum.put(localDate, userMeal.getCalories());
            }
        }
        Integer calories;
        UserMealWithExceed userMealWithExceed;
        for (UserMeal userMeal : mealList) {
            localDate = userMeal.getDateTime().toLocalDate();
            calories = sum.getOrDefault(localDate, 0);
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                if (calories > caloriesPerDay) {
                    userMealWithExceed = new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true);
                    userMeals.add(userMealWithExceed);
                } else {
                    userMealWithExceed = new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false);
                    userMeals.add(userMealWithExceed);
                }
            }
        }
        return userMeals;*/

    }
}
