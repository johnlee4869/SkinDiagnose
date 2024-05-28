package edu.whut.skinhealth.dao;

import edu.whut.skinhealth.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long>, JpaSpecificationExecutor<Record> {
    @Query(value = "SELECT r.disease, COUNT(r.disease) FROM record as r GROUP BY r.disease", nativeQuery = true)
    List<Object> findRecords();

    @Query(value="SELECT\n" +
            "    CASE\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH))  THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH), '%Y-%m')\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 2 MONTH))  THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 2 MONTH), '%Y-%m')\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 3 MONTH))  THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 3 MONTH), '%Y-%m')\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 4 MONTH))  THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 4 MONTH), '%Y-%m')\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 5 MONTH)) THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 5 MONTH), '%Y-%m')\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 6 MONTH)) THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 6 MONTH), '%Y-%m')\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 7 MONTH))  THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 7 MONTH), '%Y-%m')\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 8 MONTH))  THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 8 MONTH), '%Y-%m')\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 9 MONTH)) THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 9 MONTH), '%Y-%m')\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 10 MONTH))  THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 10 MONTH), '%Y-%m')\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 11 MONTH))  THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 11 MONTH), '%Y-%m')\n" +
            "        WHEN MONTH(time) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 12 MONTH))  THEN DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 12 MONTH), '%Y-%m')\n" +
            "    END AS month,\n" +
            "    record.disease as disease, COUNT(record.disease) as count \n" +
            "FROM\n" +
            "    record \n" +
            "WHERE\n" +
            "    time >= DATE_SUB(CURRENT_DATE(), INTERVAL 11 MONTH)\n" +
            "GROUP BY\n" +
            "    month, record.disease\n" +
            "ORDER BY\n" +
            "\t\tmonth DESC", nativeQuery = true)
    List<Object> findRecordsByMonth();
}
