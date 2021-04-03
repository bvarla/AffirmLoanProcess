package com.example.AffirmLoanProcess.Entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Loans {

    @CsvBindByName(column = "interest_rate")
    float interestRate;

    @CsvBindByName(column = "amount")
    int amount;

    @CsvBindByName(column = "id")
    int id;

    @CsvBindByName(column = "default_likelihood")
    float defaultLikelihood;

    @CsvBindByName(column = "state")
    String state;
}
