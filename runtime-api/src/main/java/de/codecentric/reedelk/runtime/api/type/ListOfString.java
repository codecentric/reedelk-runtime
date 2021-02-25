package de.codecentric.reedelk.runtime.api.type;

import de.codecentric.reedelk.runtime.api.annotation.Type;

import java.util.ArrayList;

@Type(listItemType = String.class)
public class ListOfString extends ArrayList<String> {
}
