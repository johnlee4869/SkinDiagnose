package edu.whut.skinhealth.controller;

import edu.whut.skinhealth.entity.Admin;
import edu.whut.skinhealth.entity.User;
import edu.whut.skinhealth.service.AdminService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("add")
    public ResponseEntity<String> addAdmin(String username, String password) {
        try {
            Admin admin = adminService.findAdminByUsername(username);
            if (admin != null) {
                return new ResponseEntity<>("Exist", HttpStatus.OK);
            } else {
                Md5Hash md5hash = new Md5Hash(password + username);
                Admin newAdmin = new Admin();
                newAdmin.setUsername(username);
                newAdmin.setPassword(md5hash.toHex());
                adminService.addAdmin(newAdmin);
                return new ResponseEntity<>("Success", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "login")
    public ResponseEntity<String> login(@RequestBody Admin admin) {
        try{
            Admin admin1 = adminService.findAdminByUsername(admin.getUsername());
            if (admin1 != null) {
                Md5Hash md5hash = new Md5Hash(admin.getPassword() + admin.getUsername());
                if (md5hash.toHex().equals(admin1.getPassword())) {
                    return new ResponseEntity<>("Success", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("InfoError", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("NotExist", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
