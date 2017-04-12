package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


/**
 * Created by Admin on 4/9/2017.
 */
public class MealServiceTest {
    private static ConfigurableApplicationContext appCtx;
    private static MealService mealService;
    private static ModelMatcher<Meal> matcher = new ModelMatcher<>();
    private static DbPopulator dbPopulator;

    @BeforeClass
    public static void beforeClass() {
        appCtx = new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml"});
        System.out.println("\n" + Arrays.toString(appCtx.getBeanDefinitionNames()) + "\n");
        mealService = appCtx.getBean(MealServiceImpl.class);
        dbPopulator=appCtx.getBean(DbPopulator.class);
    }

    @AfterClass
    public static void afterClass() {
        appCtx.close();
    }

    @Before
    public void setUp() throws Exception {
        // Re-initialize
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        Meal meal = mealService.get(MEAL_ID, USER_ID);
        matcher.assertEquals(meal, MEAL_1);

    }

    @Test(expected = NotFoundException.class)
    public void testGetOtherMeal() throws Exception {
        mealService.get(1, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        mealService.get(1, USER_ID);
    }

    @Test
    public void testDelete() throws Exception {
        mealService.delete(MEAL_ID, USER_ID);
        matcher.assertCollectionEquals(Arrays.asList(MEAL_9, MEAL_8, MEAL_7, MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2), mealService.getAll(USER_ID));

    }

    @Test(expected = NotFoundException.class)
    public void testDeleteOtherMeal() throws Exception {
        mealService.delete(MEAL_ID, ADMIN_ID);

    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() throws Exception {
        mealService.delete(1, USER_ID);
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        List<Meal> mealsBetween = Arrays.asList(MEAL_9, MEAL_8, MEAL_7);
        matcher.assertCollectionEquals(mealsBetween, mealService.getBetweenDateTimes(MEAL_7.getDateTime(), MEAL_9.getDateTime(), USER_ID));

    }

    @Test
    public void testGetAll() throws Exception {
        Collection<Meal> all = mealService.getAll(USER_ID);
        matcher.assertCollectionEquals(Arrays.asList(MEAL_9, MEAL_8, MEAL_7, MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2,MEAL_1), all);
    }


    @Test
    public void testUpdate() throws Exception {
        Meal updated = new Meal(MEAL_1.getId(), MEAL_1.getDateTime(), MEAL_1.getDescription(), MEAL_1.getCalories());
        updated.setDescription("UPDATED");
        updated.setCalories(100);
        updated.setDateTime(LocalDateTime.now());
        mealService.update(updated, USER_ID);
        matcher.assertEquals(updated, mealService.get(MEAL_ID, USER_ID));

    }

    @Test(expected = NotFoundException.class)
    public void testUpdateOther() throws Exception {
        Meal updated = mealService.get(MEAL_ID,USER_ID);
        updated.setDescription("UPDATED");
        updated.setCalories(100);
        updated.setDateTime(LocalDateTime.now());
        mealService.update(updated, ADMIN_ID);
    }

    @Test
    public void testSave() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.now(), "ланч", 500);
        Meal created = mealService.save(newMeal, USER_ID);
        created.setId(created.getId());
        matcher.assertCollectionEquals(Arrays.asList(created,MEAL_9, MEAL_8, MEAL_7, MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2, MEAL_1), mealService.getAll(USER_ID));
    }


}