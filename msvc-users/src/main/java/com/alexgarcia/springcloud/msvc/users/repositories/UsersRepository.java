package com.alexgarcia.springcloud.msvc.users.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.alexgarcia.springcloud.msvc.users.entities.User;

public interface UsersRepository extends CrudRepository<User, Long> {

    Optional<User> findByName(String username);

}
