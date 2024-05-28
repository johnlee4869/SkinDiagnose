package edu.whut.skinhealth.service;

import edu.whut.skinhealth.dao.UserRepository;
import edu.whut.skinhealth.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService{
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
    public User updateUserInfo(User user){
        return userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }
}
