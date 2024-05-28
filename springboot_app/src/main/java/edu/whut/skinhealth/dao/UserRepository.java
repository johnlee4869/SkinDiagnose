package edu.whut.skinhealth.dao;

import edu.whut.skinhealth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
    public User findUserByUsername(String username);
}
