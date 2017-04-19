package ru.javawebinar.topjava.repository.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUser(userRepository.get(userId));
            em.persist(meal);
            return meal;
        } else {
            if (
                    em.createNamedQuery(Meal.UPDATE)
                            .setParameter("calories", meal.getCalories())
                            .setParameter("dateTime", meal.getDateTime())
                            .setParameter("description", meal.getDescription())
                            .setParameter("userId", userId)
                            .setParameter("id", meal.getId()).executeUpdate() == 0) {
                return null;
            }

        }

        return meal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {

        return em.createNamedQuery(Meal.DELETE).setParameter("id", id).setParameter("userId", userId).executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return DataAccessUtils.singleResult(em.createNamedQuery(Meal.GET, Meal.class)
                .setParameter(1, id)
                .setParameter(2, userId)
                .getResultList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.GETALL, Meal.class)
                .setParameter(1, userId).getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {

        return em.createNamedQuery(Meal.GETBYDATE, Meal.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("userId", userId)
                .getResultList();

    }

}