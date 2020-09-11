package com.quantumtime.qc;

/**
 * .Description: Program:qc-api.Created on 2019-10-22 19:53
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

public class Target {
    @Override
    public String toString() {
        return "Target{" +
                "name='" + name + '\'' +
                ", num=" + num +
                '}';
    }

    private String name;

    private Long num;

    public String getName() {
        return name;
    }

    public Target setName(String name) {
        this.name = name;
        return this;
    }

    public Long getNum() {
        return num;
    }

    public Target setNum(Long num) {
        this.num = num;
        return this;
    }
}
