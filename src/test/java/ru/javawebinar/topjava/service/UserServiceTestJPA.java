package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * Created by Admin on 4/23/2017.
 */

@ActiveProfiles({Profiles.ACTIVE_DB, Profiles.JPA})
public class UserServiceTestJPA extends UserServiceTestGeneral {
}
