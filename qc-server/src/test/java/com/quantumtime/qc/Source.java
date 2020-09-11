package com.quantumtime.qc;

/**
 * .Description: Program:qc-api.Created on 2019-10-22 19:52
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

public class Source {
    private String name;

    private Long num;

    public String getName() {
        return name;
    }

    public Source setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Source{" +
                "name='" + name + '\'' +
                ", num=" + num +
                '}';
    }

    public Long getNum() {
        return num;
    }

    public Source setNum(Long num) {
        this.num = num;
        return this;
    }
}
