package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final ResultSetExtractor<List<User>> CUSTOM_MAPPER = new ResultSetExtractor<List<User>>() {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException {
            List<User> list = new ArrayList<>();

            User user = null;
            HashMap<Integer, User> users = new HashMap<>();
            HashMap<Integer, HashSet<Role>> roles = new HashMap<>();
            while (rs.next()) {
                int id1 = rs.getInt("id");
                if (!users.containsKey(id1)) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    boolean enabled = rs.getBoolean("enabled");
                    int calories = rs.getInt("calories_per_day");
                    String role = rs.getString("role");
                    HashSet<Role> set = new HashSet<>();
                    set.add(Role.valueOf(role));
                    roles.put(id1, set);
                    user = new User(id1, name, email, password, calories, enabled, set);
                    users.put(id1, user);

                } else {
                    roles.get(id1).add(Role.valueOf(rs.getString("role")));
                }
            }
            for (Integer integer : roles.keySet()) {
                User old = users.get(integer);
                list.add(new User(old.getId(), old.getName(),
                        old.getEmail(), old.getPassword(), old.getCaloriesPerDay(), old.isEnabled(), roles.get(integer)));
            }

            return list;
        }
    };

    private final PlatformTransactionManager dataSourceTransactionManager;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, DataSourceTransactionManager dataSourceTransactionManager, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.dataSourceTransactionManager = dataSourceTransactionManager;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        TransactionDefinition td = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(td);
        try {
            if (user.isNew()) {
//                KeyHolder keyHolder = new GeneratedKeyHolder();
//                jdbcTemplate.update(
//                        "INSERT INTO users" +
//                                " (name, email,password,registered,enabled,calories_per_day) VALUES (?,?,?,?,?,?)",
//                        user.getName(), user.getEmail(), user.getPassword(), user.getRegistered(), user.isEnabled(), user.getCaloriesPerDay(), keyHolder);
//                user.setId(keyHolder.getKey().intValue());
                Number newKey = insertUser.executeAndReturnKey(parameterSource);
                user.setId(newKey.intValue());

                for (Role role : user.getRoles()) {
                    jdbcTemplate.update(
                            "INSERT INTO user_roles (user_id, role) VALUES (?, ?)", user.getId(),
                            role.toString());
                }
                dataSourceTransactionManager.commit(transactionStatus);

            } else {
//                jdbcTemplate.batchUpdate("UPDATE users SET name=:name, email=:email, password=:password, " +
//                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id");
                jdbcTemplate.update(
                        "UPDATE users SET name=?, email=?, password=?, " +
                                "registered=?, enabled=?, calories_per_day=? WHERE id=?", user.getName(), user.getEmail(),
                        user.getPassword(), user.getRegistered(), user.isEnabled(), user.getCaloriesPerDay(), user.getId());
                for (Role role : user.getRoles()) {
                    jdbcTemplate.update(
                            "UPDATE user_roles SET role=? WHERE user_id=?", role.toString(), user.getId());
                }
                dataSourceTransactionManager.commit(transactionStatus);


            }
        } catch (DataAccessException e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            throw new DuplicateKeyException(e.getMessage());
        }

        return user;
    }

    @Override
    public boolean delete(int id) {
        TransactionDefinition td = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(td);
        boolean deleteResult = false;
        try {
            deleteResult = jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
        } catch (DataAccessException e) {
            dataSourceTransactionManager.rollback(transactionStatus);
        }
        return deleteResult;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate
                .query("SELECT users.id, users.email,users.password, users.name,users.enabled, user_roles.role,users.calories_per_day FROM users JOIN user_roles ON users.id = user_roles.user_id WHERE id=?", CUSTOM_MAPPER, id);

        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT users.id, users.email,users.password, users.name,users.enabled, user_roles.role,users.calories_per_day FROM users JOIN user_roles ON users.id = user_roles.user_id WHERE email=?", CUSTOM_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT users.id, users.email,users.password, users.name,users.enabled, user_roles.role,users.calories_per_day FROM users JOIN user_roles ON users.id = user_roles.user_id ORDER BY name, email", CUSTOM_MAPPER);
    }
}
