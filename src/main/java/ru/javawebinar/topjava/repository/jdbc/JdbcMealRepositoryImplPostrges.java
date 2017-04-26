package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import javax.sql.DataSource;
import java.time.LocalDateTime;

/**
 * Created by Admin on 4/26/2017.
 */
@Profile({Profiles.ACTIVE_DB})
@Repository
public class JdbcMealRepositoryImplPostrges extends JdbcMealRepositoryImpl {
    public JdbcMealRepositoryImplPostrges(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(dataSource, jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected LocalDateTime getConvertedLocalDataTime(LocalDateTime localDateTime) {
        return localDateTime;
    }

    @Override
    protected LocalDateTime getConvertedLocalDataTime(Meal meal) {
        return meal.getDateTime();
    }

    @Override
    public Meal getWithUser(int id) {
        throw new UnsupportedOperationException();

    }
}
