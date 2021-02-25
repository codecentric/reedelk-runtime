package de.codecentric.reedelk.runtime.api.type;

import de.codecentric.reedelk.runtime.api.annotation.Type;

import java.util.HashMap;

@Type(mapKeyType = String.class, mapValueType = ListOfString.class)
public class MapOfStringListOfString extends HashMap<String, ListOfString> {
}
