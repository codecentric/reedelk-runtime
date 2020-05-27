package com.reedelk.runtime.api.type;

import com.reedelk.runtime.api.annotation.Type;

import java.util.ArrayList;

@Type(listItemType = String.class)
public class ListOfString extends ArrayList<String> {
}
