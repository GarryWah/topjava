package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDateTime;

/**
 * Created by Admin on 4/30/2017.
 */
@Controller
@SessionAttributes(names = "addMeal")
public class AddMealController {
    @Autowired
    private MealService service;
    @RequestMapping("/addMeal")
    public String create(@RequestParam String dateTime,@RequestParam String description,@RequestParam String calories) {
        final Meal meal = new Meal(
                LocalDateTime.parse(dateTime),
                description,
                Integer.valueOf(calories));
        service.save(meal, AuthorizedUser.id());
        return "redirect:/meals";
    }

}
