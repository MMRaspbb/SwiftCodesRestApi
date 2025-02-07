package com.swift.restapi.data.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ISO2BanksResource {
    private String countryISO2;
    private String countryName;
    private List<BankResource> swiftCodes;
}
