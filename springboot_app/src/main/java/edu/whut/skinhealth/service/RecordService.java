package edu.whut.skinhealth.service;

import edu.whut.skinhealth.po.DiseaseCount;
import edu.whut.skinhealth.po.DiseaseCountByMonth;
import edu.whut.skinhealth.entity.Record;

import edu.whut.skinhealth.dao.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordService {
    @Autowired
    private RecordRepository recordRepository;

    public void addRecord(Record record){
        recordRepository.save(record);
    }
    public List<Record> getAllRecords()
    {
        return recordRepository.findAll();
    }

    public List<DiseaseCount> getAllDiseaseCounts() {

        List<DiseaseCount> list = new ArrayList<>();
        List<Object> result = recordRepository.findRecords();
        for (Object o : result) {
            Object[] rowArray = (Object[]) o;
            DiseaseCount dc = new DiseaseCount();
            dc.setDisease((String) rowArray[0]);
            dc.setCount((Long) rowArray[1]);
            list.add(dc);
        }

        return list;
    }

    public Map<String, Map<String, Long>> getAllDiseaseCountsByMonth() {

        List<DiseaseCountByMonth> list = new ArrayList<>();
        List<Object> result = recordRepository.findRecordsByMonth();
        for (Object o : result) {
            Object[] rowArray = (Object[]) o;
            DiseaseCountByMonth dcbm = new DiseaseCountByMonth();
            dcbm.setTime((String) rowArray[0]);
            dcbm.setDisease((String) rowArray[1]);
            dcbm.setCount((Long) rowArray[2]);
            list.add(dcbm);
        }

        Map<String, Map<String, Long>> data = new HashMap<>();
        for (DiseaseCountByMonth dcbm : list) {
            String time = dcbm.getTime();
            String disease = dcbm.getDisease();
            Long count = dcbm.getCount();

            if (!data.containsKey(time)) {
                Map<String, Long> diseaseCount = new HashMap<>();
                data.put(time, diseaseCount);
            }
            data.get(time).put(disease, count);
        }
        return data;
    }


}
