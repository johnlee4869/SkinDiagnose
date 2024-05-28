package edu.whut.skinhealth.dao;


import edu.whut.skinhealth.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminRepository extends JpaRepository<Admin, Long>{
    public Admin findAdminByUsername(String username);
}
