package edu.whut.skinhealth.service;

import edu.whut.skinhealth.dao.AdminRepository;
import edu.whut.skinhealth.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public boolean addAdmin(Admin admin) {
        try{
            adminRepository.save(admin);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public Admin findAdminByUsername(String username) {
        return adminRepository.findAdminByUsername(username);
    }

}
