package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.annotation.DisplayName;

public enum ParameterLocation {

    @DisplayName("Query")
    query,
    @DisplayName("Header")
    header,
    @DisplayName("Path")
    path,
    @DisplayName("Cookie")
    cookie;
}
