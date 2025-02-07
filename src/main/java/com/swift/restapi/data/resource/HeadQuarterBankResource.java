package com.swift.restapi.data.resource;

import com.swift.restapi.entities.Bank;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HeadQuarterBankResource extends BankResource {
    private final String countryName;
    private final List<BankResource> branches = new ArrayList<>();

    public HeadQuarterBankResource(Bank bank, List<Bank> bankList) {
        super(bank);
        countryName = bank.getCountryName();
        for (Bank creatorBank : bankList) {
            branches.add(new BankResource(creatorBank));
        }
    }
}
