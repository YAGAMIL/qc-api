package com.quantumtime.qc;

/**
 * .Description:风险概率 Program:qc-api.Created on 2019-11-20 14:59
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

public class RiskProbability {
    private String code;

    private double probability;

    public String getCode() {
        return code;
    }

    public RiskProbability setCode(String code) {
        this.code = code;
        return this;
    }

    public double getProbability() {
        return probability;
    }

    public RiskProbability setProbability(double probability) {
        this.probability = probability;
        return this;
    }
}
