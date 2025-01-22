package com.alexgarcia.springcloud.msvc.users.services;

import java.util.Optional;

import com.alexgarcia.springcloud.msvc.users.entities.User;

public interface IUserService {

    Optional<User> findById(Long id);

    Optional<User> findByName(String username);

    Iterable<User> getAllUsers();

    User saveUser(User user);

    Optional<User> updateUser(User user, Long id);

    void deleteUserById(Long id);


    

}
