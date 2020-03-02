package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.util.List;

@AutocompleteType
public interface ResultRow {

    @AutocompleteItem(signature = "getColumnCount()", description = "Returns column count.")
    int getColumnCount();

    @AutocompleteItem(signature = "getColumnName(columnIndex)", cursorOffset = 12, description = "Given the column index, returns column name.")
    String getColumnName(int columnIndex);

    @AutocompleteItem(signature = "columnNames()", description = "Returns a list of column names.")
    List<String> columnNames();

    @AutocompleteItem(signature = "get(columnIndex)", cursorOffset = 12, description = "Returns the object for the given column index.")
    Object get(int columnIndex);

    @AutocompleteItem(signature = "row()", description = "Returns all the objects belonging to this row.")
    List<Object> row();

}
