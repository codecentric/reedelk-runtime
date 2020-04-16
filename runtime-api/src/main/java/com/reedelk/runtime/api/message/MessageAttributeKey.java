package com.reedelk.runtime.api.message;

public interface MessageAttributeKey {

    /*
     * Attribute Key used to set the correlationId context
     * variable available in each instance of a flow execution.
     */
    String CORRELATION_ID = "correlationId";

    /*
     * Attribute Key used to identify the component name which
     * added the Message Attributes.
     */
    String COMPONENT_NAME = "component";
}
