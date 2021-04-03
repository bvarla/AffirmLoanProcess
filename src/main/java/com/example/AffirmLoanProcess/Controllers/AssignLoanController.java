package com.example.AffirmLoanProcess.Controllers;

import com.example.AffirmLoanProcess.service.AssignLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/assignLoan")
public class AssignLoanController {


    @Autowired
    AssignLoanService assignLoanService;

    @GetMapping
    public void processLoan(){
        assignLoanService.initializeData();
        assignLoanService.processLoan();
    }

}
