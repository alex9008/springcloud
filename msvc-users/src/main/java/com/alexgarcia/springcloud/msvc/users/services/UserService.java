package com.alexgarcia.springcloud.msvc.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alexgarcia.springcloud.msvc.users.entities.Role;
import com.alexgarcia.springcloud.msvc.users.entities.User;
import com.alexgarcia.springcloud.msvc.users.repositories.RoleRepository;
import com.alexgarcia.springcloud.msvc.users.repositories.UsersRepository;



@Component
public class UserService implements IUserService {

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UsersRepository usersRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User saveUser(User user) {

        user.setRoles(getRoles(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return usersRepository.save(user);
    }

    @Transactional
    public Optional<User> updateUser(User user, Long id) {

        Optional<User> userOptional = usersRepository.findById(id);

        return userOptional.map(u -> {
            u.setName(null != user.getName() ? user.getName() : u.getName());
            u.setEmail(null != user.getEmail() ? user.getEmail() : u.getEmail());
            u.setPassword(null != user.getPassword() ? passwordEncoder.encode(user.getPassword()) : u.getPassword());
            u.setEnabled(false != user.isEnabled() ? user.isEnabled() : u.isEnabled());

            u.setRoles(getRoles(user));
            return Optional.ofNullable(usersRepository.save(u));
        }).orElseGet(() -> Optional.empty());
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return usersRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByName(String username) {
        return usersRepository.findByName(username);
    }

    @Transactional(readOnly = true)
    public Iterable<User> getAllUsers() {
        return usersRepository.findAll();
    }

    @Transactional
    public void deleteUserById(Long id) {
        usersRepository.deleteById(id);
    }

    private List<Role> getRoles(User user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> roleOptional = roleRepository.findByName("ROLE_USER");
        roleOptional.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
            adminRoleOptional.ifPresent(roles::add);
        }
        return roles;
    }

}
