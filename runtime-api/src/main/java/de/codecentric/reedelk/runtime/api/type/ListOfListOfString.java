package de.codecentric.reedelk.runtime.api.type;

import de.codecentric.reedelk.runtime.api.annotation.Type;

import java.util.ArrayList;

@Type(listItemType = ListOfString.class)
public class ListOfListOfString extends ArrayList<ListOfString> {
}
