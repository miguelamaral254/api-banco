package br.com.apibanco.domain.models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SAVINGS")
public class SavingsAccount extends Account {

    @Column(nullable = false)
    private double interestRate;

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
