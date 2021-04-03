package com.example.AffirmLoanProcess.service;

import com.example.AffirmLoanProcess.Entity.*;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Service
public class AssignLoanService {

    public static final String ASSIGNMENT = "Assignment";
    public static final String YIELD = "Yield";


    List<Banks> banksList;
    List<Covenants> covenantsList;
    List<Facilities> facilitiesList;
    List<Loans> loansList;

    List<Assignment> assignmentList ;
    List<Yields> yieldsList ;

    public void initializeData() {

        assignmentList = new ArrayList<>();
        yieldsList = new ArrayList<>();

        try {
            csvConvertToObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void processLoan(){
        for (Loans loan : loansList) {
            facilityApproved(loan);
        }
        generateCSV(YIELD);
        generateCSV(ASSIGNMENT);

    }


    public void facilityApproved(Loans loan){

        for (Facilities facilitie : facilitiesList) {
            if(facilitie.getAmount() >= loan.getAmount()){
                if(loanPassed(loan,facilitie.getBankId())){
                    facilitie.setAmount(facilitie.getAmount() -loan.getAmount());
                    Assignment assignment = new Assignment();
                    assignment.setLoanId(loan.getId());
                    assignment.setFacilityId(facilitie.getFacilityId());
                    assignmentList.add(assignment);
                    calculateYields(loan,facilitie);
                    break;
                }
            }
        }
    }


    public void calculateYields(Loans loan, Facilities facilitie){

        int expected_yield;

        expected_yield = (int) ((1 - loan.getDefaultLikelihood())
                * loan.getInterestRate() * loan.getAmount()
                - loan.getDefaultLikelihood() * loan.getAmount()
                - facilitie.getInterestRate() * loan.getAmount());

        for (Yields yield : yieldsList) {
            if(yield.getFacilityId() == facilitie.getFacilityId()){
                yield.setExpectedYield((int) (yield.getExpectedYield()+expected_yield));
                return;
            }
        }

        Yields yield = new Yields();
        yield.setFacilityId(facilitie.getFacilityId());
        yield.setExpectedYield(expected_yield);


        yieldsList.add(yield);
    }


    public boolean loanPassed(Loans loan,int bankId){

        Predicate<Covenants> pFilter = covenant -> covenant.getMaxDefaultLikelihood() >= loan.getDefaultLikelihood()
                                                    && !covenant.getBannedState().equalsIgnoreCase(loan.getState())
                                                    && covenant.getBankId() == bankId ;
        return covenantsList
                .stream()
                .anyMatch(pFilter);
    }


    public void csvConvertToObject() throws FileNotFoundException {

        banksList = new CsvToBeanBuilder(new FileReader("src/main/resources/filesExcel/banks.csv"))
                .withType(Banks.class)
                .build()
                .parse();

        covenantsList = new CsvToBeanBuilder(new FileReader("src/main/resources/filesExcel/covenants.csv"))
                .withType(Covenants.class)
                .build()
                .parse();

        facilitiesList = new CsvToBeanBuilder(new FileReader("src/main/resources/filesExcel/facilities.csv"))
                .withType(Facilities.class)
                .build()
                .parse();

        facilitiesList.sort(Comparator.comparing(value -> value.getInterestRate()));

        loansList = new CsvToBeanBuilder(new FileReader("src/main/resources/filesExcel/loans.csv"))
                .withType(Loans.class)
                .build()
                .parse();

    }


    public void generateCSV(String type) {
        List<String[]> csvData = null;


        Path path = Paths.get("src/main/resources/OutputExcelFiles/"+type+".csv");
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(type.equalsIgnoreCase(ASSIGNMENT))
        {
            csvData = createCsvAssignmentData();
        }
        if(type.equalsIgnoreCase(YIELD))
        {
            csvData = createCsvYieldData();
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/OutputExcelFiles/"+type+".csv"))) {
            writer.writeAll(csvData);
         //   writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public List<String[]> createCsvYieldData() {
        String[] yieldHeader = {"facility_id","expected_yield"};
        List<String[]> list = new ArrayList<>();
        list.add(yieldHeader);
        for (Yields yield : yieldsList) {
            String[] data = new String[2];
            data[0] = String.valueOf(yield.getFacilityId());
            data[1] = String.valueOf(yield.getExpectedYield());
            list.add(data);
        }
        return list;
    }

    public List<String[]> createCsvAssignmentData() {
        String[] assignmentHeader = {"loan_id","facility_id"};
        List<String[]> list = new ArrayList<>();
        list.add(assignmentHeader);
        for (Assignment assignment : assignmentList) {
            String[] data = new String[2];
            data[0] = String.valueOf(assignment.getLoanId());
            data[1] = String.valueOf(assignment.getFacilityId());
            list.add(data);
        }
        return list;
    }

}
