package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.implementation.MealDaoImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.implementation.MealServiceImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Admin on 3/23/2017.
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);
    private MealService mealService = new MealServiceImpl(new MealDaoImpl());
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static String INSERT_OR_EDIT = "/editForm.jsp";
    private static String LIST_MEALS = "/meals.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        List<MealWithExceed> mealWithExceededList = MealsUtil
                .getFilteredWithExceeded(mealService.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        String forward = "";
        String action = req.getParameter("action");
        if (action == null) action = "meals";

        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(req.getParameter("id"));
            mealService.delete(mealId);
            LOG.debug("delete meal with id" + String.valueOf(mealId));
            resp.sendRedirect("/topjava/meals");return;
/*            forward = LIST_MEALS;
            req.setAttribute("meals", mealWithExceededList);*/
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(req.getParameter("id"));
            Meal meal = mealService.getById(mealId);
            LOG.debug("update meal with id" + String.valueOf(mealId));
            req.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("meals")) {
            forward = LIST_MEALS;
            LOG.debug("get meals");
            req.setAttribute("meals", mealWithExceededList);
        } else {
            forward = INSERT_OR_EDIT;
        }

        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("datatime"), formatter);
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String strId = req.getParameter("id");
        LOG.debug("In post method");
        if (strId == null || strId.isEmpty()) {

            mealService.createOrUpdate(null, localDateTime, description, calories);
        } else {
            mealService.createOrUpdate(Integer.parseInt(req.getParameter("id")), localDateTime, description, calories);
        }
//        req.setAttribute("meals", mealService.getAllMeals());
//        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        resp.sendRedirect("/topjava/meals");
    }
}
