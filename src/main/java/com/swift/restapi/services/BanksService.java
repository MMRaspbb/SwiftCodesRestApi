package com.swift.restapi.services;

import com.swift.restapi.data.resource.*;
import com.swift.restapi.entities.Bank;
import com.swift.restapi.entities.BankRelationship;
import com.swift.restapi.repositories.BankRelationshipsRepository;
import com.swift.restapi.repositories.BanksRepository;
import com.swift.restapi.utils.Verifier;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BanksService {
    private BanksRepository banksRepository;
    private BankRelationshipsRepository bankRelationshipsRepository;

    public BankResource getBankBySwiftCode(String swiftCode) {
        Bank bank = banksRepository.findBySwiftCode(swiftCode);
        if (bank == null) return null;
        BankResource bankResource;
        if (!bank.isHeadquarter()) {
            bankResource = new BranchBankResource(bank);
        } else {
            List<Bank> branchBanks = banksRepository.findBranchBanksFromHeadquartersId(bank.getId());
            bankResource = new HeadQuarterBankResource(bank, branchBanks);
        }
        return bankResource;
    }

    public ISO2BanksResource getBanksByISO2Code(String iso2Code) {
        List<Bank> banksWithISO2Code = banksRepository.findByCountryISO2(iso2Code);
        if (banksWithISO2Code.isEmpty()) {
            return null;
        }
        List<BankResource> bankResourceList = banksWithISO2Code.stream()
                .map(BankResource::new)
                .toList();
        String countryName = banksWithISO2Code.get(0).getCountryName();
        return new ISO2BanksResource(iso2Code, countryName, bankResourceList);
    }

    public ResponseMessage addBank(Bank bank) {
        String verifierMessage = Verifier.verifyBank(bank);
        if (verifierMessage != null) return new ResponseMessage(verifierMessage);
        if (bank.isHeadquarter()) {
            try {
                saveHeadquarterBank(bank);
            } catch (DataIntegrityViolationException e) {
                return new ResponseMessage("Bank with given swift-code is already in the database");
            }
        } else {
            try {
                saveBranchBank(bank);
            } catch (DataIntegrityViolationException e) {
                return new ResponseMessage("Bank with given swift-code is already in the database");
            }
        }
        return new ResponseMessage("Bank added successfully");
    }

    public ResponseMessage deleteBank(String swiftCode) {
        Bank bankToDelete = banksRepository.findBySwiftCode(swiftCode);
        if (bankToDelete == null) {
            return new ResponseMessage("Bank with given swift-code doesn't exist");
        }
        int bankToDeleteId = bankToDelete.getId();
        List<BankRelationship> bankRelationshipsToDelete = bankRelationshipsRepository.findByHeadquarterIdOrBranchId(bankToDeleteId, bankToDeleteId);
        if (!bankRelationshipsToDelete.isEmpty()) {
            bankRelationshipsRepository.deleteAll(bankRelationshipsToDelete);
        }
        banksRepository.delete(bankToDelete);
        return new ResponseMessage("Bank and it's relationships deleted successfuly");
    }

    private void saveHeadquarterBank(Bank bank) throws DataIntegrityViolationException {
        Bank addedBank = banksRepository.save(bank);
        List<Integer> banksBranchesId = banksRepository.findBranchesByPrefix(bank.getSwiftCode().substring(0, 8));
        if (!banksBranchesId.isEmpty()) {
            List<BankRelationship> relationshipsToAdd = new ArrayList<>();
            for (int id : banksBranchesId) {
                BankRelationship relationshipToAdd = new BankRelationship();
                relationshipToAdd.setBranchId(id);
                relationshipToAdd.setHeadquarterId(addedBank.getId());
                relationshipsToAdd.add(relationshipToAdd);
            }
            bankRelationshipsRepository.saveAll(relationshipsToAdd);
        }
    }

    private void saveBranchBank(Bank bank) throws DataIntegrityViolationException {
        Bank addedBank = banksRepository.save(bank);
        Integer banksHeadquarter = banksRepository.findHeadquarterByPrefix(bank.getSwiftCode().substring(0, 8));
        if (banksHeadquarter != null) {
            BankRelationship relationshipToAdd = new BankRelationship();
            relationshipToAdd.setHeadquarterId(banksHeadquarter);
            relationshipToAdd.setBranchId(addedBank.getId());
            bankRelationshipsRepository.save(relationshipToAdd);
        }
    }
}
