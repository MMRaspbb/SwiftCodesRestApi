package com.swift.restapi.data.resource;

import com.swift.restapi.entities.Bank;
import lombok.Getter;

@Getter
public class BranchBankResource extends BankResource {
    private String countryName;

    public BranchBankResource(Bank bank) {
        super(bank);
        this.countryName = bank.getCountryName();
    }
}
