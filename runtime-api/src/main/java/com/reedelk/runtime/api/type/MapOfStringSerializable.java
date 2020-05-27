package com.reedelk.runtime.api.type;

import com.reedelk.runtime.api.annotation.Type;

import java.io.Serializable;
import java.util.HashMap;

@Type(mapKeyType = String.class, mapValueType = Serializable.class)
public class MapOfStringSerializable extends HashMap<String, Serializable> {
}
