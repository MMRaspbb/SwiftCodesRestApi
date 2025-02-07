package com.swift.restapi.repositories;

import com.swift.restapi.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BanksRepository extends JpaRepository<Bank, Long> {
    @Query("SELECT b.swiftCode FROM Bank b")
    List<String> findAllSwiftCodes();

    List<Bank> findByIsHeadquarter(boolean isHeadquarter);

    Bank findBySwiftCode(String swiftCode);

    @Query("SELECT b FROM BankRelationship br INNER JOIN Bank b ON b.id = br.branchId WHERE br.headquarterId = :id")
    List<Bank> findBranchBanksFromHeadquartersId(int id);

    List<Bank> findByCountryISO2(String countryISO2);

    @Query("SELECT b.id FROM Bank b WHERE SUBSTRING(b.swiftCode, 1, 8) = :prefix AND b.isHeadquarter = FALSE")
    List<Integer> findBranchesByPrefix(String prefix);

    @Query("SELECT b.id FROM Bank b WHERE SUBSTRING(b.swiftCode, 1, 8) = :prefix AND b.isHeadquarter = TRUE")
    Integer findHeadquarterByPrefix(String prefix);
}
