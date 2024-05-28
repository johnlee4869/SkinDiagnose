package edu.whut.skinhealth.controller;

import edu.whut.skinhealth.entity.User;
import edu.whut.skinhealth.po.DiseaseCount;
import edu.whut.skinhealth.entity.Record;
import edu.whut.skinhealth.po.RecordRequest;
import edu.whut.skinhealth.po.UserInfo;
import edu.whut.skinhealth.service.RecordService;
import edu.whut.skinhealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private UserService userService;

    @GetMapping("queryCount")
    private ResponseEntity<List<DiseaseCount>> queryCount(){
        try {
            return new ResponseEntity<>(recordService.getAllDiseaseCounts(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("queryByMonth")
    private ResponseEntity<Map<String, Map<String, Long>>> queryCountByMonth(){
        try {
            return new ResponseEntity<>(recordService.getAllDiseaseCountsByMonth(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    private ResponseEntity<String> addRecord(@RequestBody RecordRequest recordRequest){
        try{
            User user = userService.findUserByUsername(recordRequest.getUsername());
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setBirthday(user.getBirthday());
            userInfo.setGender(user.getGender());
            userInfo.setDistrict(user.getDistrict());

            Record record = new Record();
            record.setUser(userInfo);
            record.setDisease(recordRequest.getDisease());
            record.setTime(new Timestamp(new Date(System.currentTimeMillis()).getTime()));

            recordService.addRecord(record);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("query")
    private ResponseEntity<List<Record>> queryAllRecords(){
        try{
            return new ResponseEntity<>(recordService.getAllRecords(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
