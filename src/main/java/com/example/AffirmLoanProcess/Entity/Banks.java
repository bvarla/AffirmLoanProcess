package com.example.AffirmLoanProcess.Entity;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Banks {

    @CsvBindByName(column = "bank_id")
    int bankId;

    @CsvBindByName(column = "name")
    String name;

}
