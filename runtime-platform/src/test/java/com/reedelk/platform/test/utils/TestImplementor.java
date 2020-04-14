package com.reedelk.platform.test.utils;

import com.reedelk.runtime.api.component.Implementor;

import java.math.BigDecimal;

public class TestImplementor implements Implementor {

    private String property1;
    private BigDecimal property2;

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public BigDecimal getProperty2() {
        return property2;
    }

    public void setProperty2(BigDecimal property2) {
        this.property2 = property2;
    }
}
