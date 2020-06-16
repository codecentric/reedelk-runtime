package com.reedelk.runtime.api.type;

import com.reedelk.runtime.api.annotation.Type;

import java.util.HashMap;

@Type(mapKeyType = String.class, mapValueType = String.class)
public class MapOfStringString extends HashMap<String, String> {
}
