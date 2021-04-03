package com.example.AffirmLoanProcess.Entity;


import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Facilities {

    @CsvBindByName(column = "id")
    int facilityId;

    @CsvBindByName(column = "bank_id")
    int bankId;

    @CsvBindByName(column = "amount")
    float amount;

    @CsvBindByName(column = "interest_rate")
    float interestRate;

}
