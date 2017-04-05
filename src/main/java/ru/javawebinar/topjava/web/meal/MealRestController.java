package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Controller
public class MealRestController {
    private static final Logger LOG = LoggerFactory.getLogger(MealRestController.class);
    @Autowired
    private MealService service;

    public List<Meal> getAll(int userId) {
        return service.getAll(userId);
    }

    public List<Meal> getFilteredByDate(LocalDate startDate, LocalDate endDate, int userId) {
        LOG.info("filter by date");
        return (List<Meal>) service.getFilteredByDate(startDate, endDate, userId);
    }

    public List<Meal> getFilteredByTime(LocalTime startTime, LocalTime endTime, int userId) {
        LOG.info("filter by time");
        return (List<Meal>) service.getFilteredByTime(startTime, endTime, userId);
    }

    public Meal saveById(Meal meal) {
        LOG.info("save meal with id= " + meal.getId());
        if (!checkMealOnUserId(meal))
            throw new NotFoundException("not found");
        return service.save(meal, AuthorizedUser.id());
    }

    public Meal getById(int id) {
        LOG.info("get meal with id= " + id);
        Meal meal = service.get(id, AuthorizedUser.id());
        if (!checkMealOnUserId(meal))
            throw new NotFoundException("not found");
        return meal;
    }

    public void delete(int id) {
        LOG.info("delete meal with id= " + id);
        if (getById(id) != null)
            service.delete(id, AuthorizedUser.id());
    }

    public List<MealWithExceed> getWithExceed(int calories, int userId) {
        return MealsUtil.getWithExceeded(getAll(userId), calories);
    }

    public List<MealWithExceed> getWithExceedFilteredByTime(LocalTime startTime, LocalTime endTime, int calories, int userId) {
        return MealsUtil.getFilteredWithExceeded(getAll(userId), startTime, endTime, calories);
    }

    public void processGET(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")), AuthorizedUser.id());
        LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        saveById(meal);
        response.sendRedirect("meals");
    }

    public void processPOST(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                LOG.info("Delete {}", id);
//                repository.delete(id, AuthorizedUser.id());
                delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = action.equals("create") ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, AuthorizedUser.id()) :
                        getById(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                break;
            case "all":
            default:
                LOG.info("getAll");
                request.setAttribute("meals",
                        MealsUtil.getWithExceeded(getAll(AuthorizedUser.id()), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    private boolean checkMealOnUserId(Meal meal) {
        return meal.getUserId() == AuthorizedUser.id();
    }
}