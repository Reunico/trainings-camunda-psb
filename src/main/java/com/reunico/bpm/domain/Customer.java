package com.reunico.bpm.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer implements Serializable {
    @JsonAlias("name.findName")
    private String fullName;
    @JsonAlias("name.title")
    private String title;
    @JsonAlias("random.number")
    private Long number;
    @JsonAlias("finance.amount")
    private BigDecimal amount;
    @JsonAlias("address.countryCode")
    private String countryCode;

    public Customer() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "fullName='" + fullName + '\'' +
                ", title='" + title + '\'' +
                ", number=" + number +
                ", amount=" + amount +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
