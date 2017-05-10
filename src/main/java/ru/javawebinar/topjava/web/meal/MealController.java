package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Admin on 4/29/2017.
 */
@Controller
@RequestMapping("/meals")
@SessionAttributes(names = "meal")
public class MealController {
    @Autowired
    private MealService service;

    @ModelAttribute("meal")
    public Meal getForm() {
        return new Meal();
    }

    @RequestMapping
    public String show(Model model) {

        List<Meal> all = service.getAll(AuthorizedUser.id());
        model.addAttribute("mealsList", MealsUtil.getWithExceeded(all, AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }
    @RequestMapping(value = "/filter",method = POST)
    public String filter(Model model,@RequestParam String startDate,@RequestParam String endDate,@RequestParam String startTime,@RequestParam String endTime){
        LocalDate startDateP = DateTimeUtil.parseLocalDate(startDate);
        LocalDate endDateP = DateTimeUtil.parseLocalDate(endDate);
        LocalTime startTimeP = DateTimeUtil.parseLocalTime(startTime);
        LocalTime endTimeP = DateTimeUtil.parseLocalTime(endTime);
        model.addAttribute("mealsList", MealsUtil.getFilteredWithExceeded(
                service.getBetweenDates(
                        startDate != null ? startDateP : DateTimeUtil.MIN_DATE,
                        endDate != null ? endDateP : DateTimeUtil.MAX_DATE, AuthorizedUser.id()),
                startTime != null ? startTimeP : LocalTime.MIN,
                endTime != null ? endTimeP : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()
        ));
        return "meals";
    }

    public Meal get(int id) {
        return get(id);
    }

    @RequestMapping(value = "/delete/{id}", method = GET)
    public String delete(@PathVariable int id) {
        service.delete(id, AuthorizedUser.id());
        return "redirect:/meals";
    }

    //    public String getAll(Model model) {
//        model.addAttribute("meals",service.getAll(AuthorizedUser.id()))
//        return service.getAll(AuthorizedUser.id());
//    }
    @RequestMapping("/create")
    public String create(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
//        service.save(meal, AuthorizedUser.id());
        return "meal";
    }

    @RequestMapping(value = "/update/{id}")
    public String update(Model model, @PathVariable int id) {
        model.addAttribute("meal", service.get(id, AuthorizedUser.id()));
        return "meal";
    }

    public List<MealWithExceed> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return getBetween(startDate, startTime, endDate, endTime);
    }

    @RequestMapping(method = POST)
    public String addMeal(@RequestParam String dateTime, @RequestParam String description, @RequestParam String calories,@RequestParam String id) {
        final Meal meal = new Meal(
                LocalDateTime.parse(dateTime),
                description,
                Integer.valueOf(calories));
        if (id=="") {
            service.save(meal, AuthorizedUser.id());
        }
        else{
            Meal updated = service.get(Integer.parseInt(id), AuthorizedUser.id());
            updated.setDateTime(LocalDateTime.parse(dateTime));
            updated.setDescription(description);
            updated.setCalories(Integer.valueOf(calories));
            service.update(updated, AuthorizedUser.id());
        }
        return "redirect:meals";
    }
//    @RequestMapping("/cancel")
//    public String cancel(SessionStatus status) {
//        status.setComplete();
//        return "redirect:meals";
//    }

}
