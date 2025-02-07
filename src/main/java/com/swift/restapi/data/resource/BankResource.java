package com.swift.restapi.data.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swift.restapi.entities.Bank;
import lombok.Getter;

@Getter

public class BankResource {
    protected String address;
    protected String bankName;
    protected String countryISO2;
    protected boolean isHeadquarter;
    protected String swiftCode;

    @JsonProperty("isHeadquarter")
    public boolean isHeadquarter() {
        return isHeadquarter;
    }

    public BankResource(Bank bank) {
        address = bank.getAddress();
        bankName = bank.getBankName();
        countryISO2 = bank.getCountryISO2();
        isHeadquarter = bank.isHeadquarter();
        swiftCode = bank.getSwiftCode();
    }
}
