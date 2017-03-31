package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.NamedEntity;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository

public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        LOG.info("delete " + id);
        if (repository.remove(id)!=null) {
            return true;
        }
        return false;
    }

    @Override
    public User save(User user) {
        if (user.isNew())
            user.setId(counter.incrementAndGet());
        repository.put(user.getId(), user);
        LOG.info("save " + user);
        return user;
    }

    @Override
    public User get(int id) {
        LOG.info("get " + id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        LOG.info("getAll");
        List<User> users = (List<User>) repository.values();
        Collections.sort(users, Comparator.comparing(NamedEntity::getName));
        return users;
    }

    @Override
    public User getByEmail(String email) {
//        for (User us : repository.values()) {
//
//            if (us.getEmail().equals(email)) {
//                LOG.info("getByEmail " + email);
//                return us;
//            }
//        }
//        LOG.info("not found user with " + email);
//        return null;
        LOG.info("getByEmail " + email);
        return repository.values().stream().filter(user -> user.getEmail().equals(email)).findAny().orElse(null);
    }
}
