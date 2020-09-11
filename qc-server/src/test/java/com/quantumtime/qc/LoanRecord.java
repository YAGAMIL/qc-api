package com.quantumtime.qc;

import com.quantumtime.qc.entity.activity.HeatRule;

/**
 * .Description:贷款记录 Program:qc-api.Created on 2019-11-20 14:53
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public class LoanRecord {
    private double capital;

    private double interest;

    private String riskLevel;

    public double getCapital() {
        return capital;
    }

    public LoanRecord setCapital(double capital) {
        this.capital = capital;
        return this;
    }

    public double getInterest() {
        return interest;
    }

    public LoanRecord setInterest(double interest) {
        this.interest = interest;
        return this;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public LoanRecord setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
        return this;
    }

    public static void main(String[] args) {
        HeatRule rule = HeatRule.builder().like(7).score(8).related(9).build().init();
        System.err.println(rule);
    }
}
