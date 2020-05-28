package com.reedelk.runtime.api.type;

import com.reedelk.runtime.api.annotation.Type;

import java.util.ArrayList;

@Type(listItemType = ListOfString.class)
public class ListOfListOfString extends ArrayList<ListOfString> {
}
