package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Admin on 4/26/2017.
 */
@Profile({Profiles.HSQL_DB})
@Repository
public class JdbcMealRepositoryImplHSQLDB extends JdbcMealRepositoryImpl {
    public JdbcMealRepositoryImplHSQLDB(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(dataSource, jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected Timestamp getConvertedLocalDataTime(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    @Override
    protected Timestamp getConvertedLocalDataTime(Meal meal) {
        return Timestamp.valueOf(meal.getDateTime());
    }

    @Override
    public Meal getWithUser(int id) {
        throw new UnsupportedOperationException();

    }
}
