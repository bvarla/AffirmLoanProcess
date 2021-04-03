package com.example.AffirmLoanProcess.Entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Covenants {

    @CsvBindByName(column = "facility_id")
    int facilityId;

    @CsvBindByName(column = "max_default_likelihood")
    float maxDefaultLikelihood;

    @CsvBindByName(column = "bank_id")
    int bankId;

    @CsvBindByName(column = "banned_state")
    String bannedState;
}
