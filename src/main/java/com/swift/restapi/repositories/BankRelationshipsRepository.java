package com.swift.restapi.repositories;

import com.swift.restapi.entities.BankRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRelationshipsRepository extends JpaRepository<BankRelationship, Long> {
    List<BankRelationship> findByHeadquarterIdOrBranchId(int headquarterId, int branchId);

    @Query("SELECT br FROM BankRelationship br")
    List<BankRelationship> findAllRelationships();
}
