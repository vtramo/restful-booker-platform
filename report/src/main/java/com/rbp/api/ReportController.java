package com.rbp.api;

import com.rbp.model.report.Report;
import com.rbp.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Report> getAllRoomReports(@CookieValue(value ="token") String token) {
        Report report = reportService.getAllRoomsReport(token);

        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

    @RequestMapping(value = "/room/{id:[0-9]*}", method = RequestMethod.GET)
    public ResponseEntity<Report> getSpecificRoomReport(@PathVariable(value = "id") int roomId){
        Report report = reportService.getSpecificRoomReport(roomId);

        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

}
