package com.kimon.project_management_system.service;

import com.kimon.project_management_system.model.User;

public interface UserService {

    User findByEmail(String email) throws Exception;

    User findUserById(long userId) throws Exception;

    User updateUserProjectSize(User user, int number);
}
