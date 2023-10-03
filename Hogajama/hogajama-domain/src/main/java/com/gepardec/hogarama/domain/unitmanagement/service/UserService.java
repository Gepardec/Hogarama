package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.dao.UserDAO;
import com.gepardec.hogarama.domain.unitmanagement.dao.UnitDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;

@RequestScoped
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Inject
    private UserDAO userDao;
    @Inject
    private UnitDAO unitDao;

    public Optional<User> getRegisteredUser(String userKey) {
        return userDao.getByKey(userKey);
    }

    @Transactional
    public User register(String userKey) {
        LOG.info("Register new user with key {}.", userKey);
        User user = new User();
        user.setKey(userKey);
        userDao.save(user);
        Unit defaultUnit = Unit.createDefault(user);
        unitDao.save(defaultUnit);
        user.addUnit(defaultUnit);
        return user;
    }

}
