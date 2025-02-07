package com.swift.restapi.services;

import com.swift.restapi.entities.Bank;
import com.swift.restapi.entities.BankRelationship;
import com.swift.restapi.repositories.BankRelationshipsRepository;
import com.swift.restapi.repositories.BanksRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SwiftFileParserService {
    private BanksRepository banksRepository;
    private BankRelationshipsRepository bankRelationshipsRepository;
    private static final String filePath = "data/swift_codes.tsv";
    private static final int countryISO2CodeColumn = 0;
    private static final int swiftCodeColumn = 1;
    private static final int nameColumn = 3;
    private static final int addressColumn = 4;
    private static final int countryNameColumn = 6;

    @PostConstruct
    public void parseFileOnStartup() {
        String line;
        String delimiter = "\t";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            List<Bank> bankList = new ArrayList<>();
            HashSet<String> dbSwiftCodes = new HashSet<>(banksRepository.findAllSwiftCodes());
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("\"", "");
                String[] columns = line.split(delimiter);
                String swiftCode = columns[swiftCodeColumn];

                if (dbSwiftCodes.contains(swiftCode)) continue;

                Bank bank = new Bank();
                boolean isHeadquarter = swiftCode.endsWith("XXX");
                bank.setCountryISO2(columns[countryISO2CodeColumn]);
                bank.setSwiftCode(swiftCode);
                bank.setIsHeadquarter(isHeadquarter);
                bank.setBankName(columns[nameColumn]);
                bank.setAddress(columns[addressColumn]);
                bank.setCountryName(columns[countryNameColumn]);

                bankList.add(bank);
            }

            if (!bankList.isEmpty()) {
                banksRepository.saveAll(bankList);
            } else {
                return;
            }
            List<Bank> headquarterBankList = banksRepository.findByIsHeadquarter(true);
            Map<String, Integer> headquarterBankPrefixToIndexMap = headquarterBankList.stream()
                    .collect(Collectors.toMap(
                            bank -> bank.getSwiftCode().substring(0, 8),
                            Bank::getId
                    ));
            List<Bank> branchBankList = banksRepository.findByIsHeadquarter(false);
            HashSet<BankRelationship> relationshipsSet = new HashSet<>(bankRelationshipsRepository.findAllRelationships());
            for (Bank branch : branchBankList) {
                String branchSwiftCodeSubstring = branch.getSwiftCode().substring(0, 8);
                if (headquarterBankPrefixToIndexMap.containsKey(branchSwiftCodeSubstring)) {
                    BankRelationship relationship = new BankRelationship();
                    relationship.setHeadquarterId(headquarterBankPrefixToIndexMap.get(branchSwiftCodeSubstring));
                    relationship.setBranchId(branch.getId());
                    if (!relationshipsSet.contains(relationship)) {
                        bankRelationshipsRepository.save(relationship);
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
