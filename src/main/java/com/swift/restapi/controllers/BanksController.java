package com.swift.restapi.controllers;

import com.swift.restapi.data.resource.BankResource;
import com.swift.restapi.data.resource.ISO2BanksResource;
import com.swift.restapi.data.resource.ResponseMessage;
import com.swift.restapi.entities.Bank;
import com.swift.restapi.services.BanksService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/v1")
public class BanksController {
    private BanksService banksService;

    @GetMapping("/swift-codes/{swiftCode}")
    public BankResource getBankBySwiftCode(@PathVariable String swiftCode) {
        return banksService.getBankBySwiftCode(swiftCode);
    }

    @GetMapping("/swift-codes/country/{countryISO2code}")
    public ISO2BanksResource getBanksByISO2Code(@PathVariable String countryISO2code) {
        return banksService.getBanksByISO2Code(countryISO2code);
    }

    @PostMapping("/swift-codes")
    public ResponseMessage addBank(@RequestBody Bank bank) {
        return banksService.addBank(bank);
    }

    @DeleteMapping("/swift-codes/{swiftCode}")
    public ResponseMessage deleteBank(@PathVariable String swiftCode) {
        return banksService.deleteBank(swiftCode);
    }
}
