package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.util.exception.NotFoundException;

/**
 * Created by Admin on 4/22/2017.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public abstract class ServiceTest{
    static {
        // needed only for java.util.logging (postgres driver)
        SLF4JBridgeHandler.install();
    }
    @Test
    public abstract void testSave() throws Exception;

    @Test
    public abstract void testDelete() throws Exception;

    @Test(expected = NotFoundException.class)
    public abstract void testNotFoundDelete() throws Exception;

    @Test
    public abstract void testGet() throws Exception;

    @Test(expected = NotFoundException.class)
    public abstract void testGetNotFound() throws Exception;

    @Test
    public abstract void testUpdate() throws Exception;

    @Test
    public abstract void testGetAll() throws Exception;



}
