package com.swift.restapi.utils;

import com.swift.restapi.entities.Bank;

public class Verifier {
    public static String verifyBank(Bank bank){
        String swiftCode = bank.getSwiftCode();
        if(swiftCode.length() != 11) {
            return "Swift-code has incorrect lenght!";
        }
        if(bank.isHeadquarter() && !swiftCode.startsWith("XXX", 8)) {
            return "Bank is marked as headquarter but swift-code doesn't end with XXX!";
        }
        if(!bank.isHeadquarter() && swiftCode.startsWith("XXX", 8)) {
            return "Bank is marked as not headquarter but swift-code ends with XXX!";
        }
        if(bank.getCountryISO2().length() != 2) {
            return "CountryISO2 has incorrect lenght";
        }
        return null;
    }
}
