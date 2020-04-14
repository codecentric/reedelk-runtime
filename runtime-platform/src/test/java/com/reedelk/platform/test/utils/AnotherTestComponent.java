package com.reedelk.platform.test.utils;

import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

public class AnotherTestComponent implements ProcessorSync {

    private int property1;
    private long property2;
    private String property3;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException("Test Only ProcessorSync");
    }

    public int getProperty1() {
        return property1;
    }

    public void setProperty1(int property1) {
        this.property1 = property1;
    }

    public long getProperty2() {
        return property2;
    }

    public void setProperty2(long property2) {
        this.property2 = property2;
    }

    public String getProperty3() {
        return property3;
    }

    public void setProperty3(String property3) {
        this.property3 = property3;
    }
}
