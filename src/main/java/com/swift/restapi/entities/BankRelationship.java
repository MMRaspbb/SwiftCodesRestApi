package com.swift.restapi.entities;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.Objects;

@Entity
@Setter
public class BankRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int headquarterId;
    private int branchId;

    @Override
    public String toString() {
        return id + " " + headquarterId + " " + branchId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(headquarterId, branchId);
    }
}
