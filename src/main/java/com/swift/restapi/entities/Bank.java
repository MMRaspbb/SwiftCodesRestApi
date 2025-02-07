package com.swift.restapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;
    private String swiftCode;

    public Bank() {
    }

    public void setIsHeadquarter(boolean value) {
        isHeadquarter = value;
    }

    @Override
    public String toString() {
        return id + " " + bankName;
    }
}
