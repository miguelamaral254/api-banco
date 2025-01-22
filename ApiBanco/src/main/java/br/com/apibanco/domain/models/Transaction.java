package br.com.apibanco.domain.models;

import br.com.apibanco.domain.enums.TransactionType;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private double amount;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "transfer_customer_id", nullable = true)
    private Customer transferCustomer;

    @Column(nullable = false, length = 1)
    private char valueType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Customer getTransferCustomer() {
        return transferCustomer;
    }

    public void setTransferCustomer(Customer transferCustomer) {
        this.transferCustomer = transferCustomer;
    }

    public char getValueType() {
        return valueType;
    }

    public void setValueType(char valueType) {
        this.valueType = valueType;
    }
}
