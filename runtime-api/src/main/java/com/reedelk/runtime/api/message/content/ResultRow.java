package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.util.List;

@AutocompleteType(
        description = "The ResultRow type encapsulates a database row."
)
public interface ResultRow extends Serializable {

    @AutocompleteItem(
            signature = "columnCount()",
            example = "row.columnCount()",
            description = "Returns the number of columns in this row.")
    int columnCount();

    @AutocompleteItem(
            signature = "columnName(index: int)",
            cursorOffset = 1,
            example = "row.columnName(4)",
            description = "Given the column index, returns the column name at the given index.")
    String columnName(int columnIndex);

    @AutocompleteItem(
            signature = "columnNames()",
            cursorOffset = 1,
            example = "row.columnNames()",
            description = "Returns a list containing all column names of this database row.")
    List<String> columnNames();

    @AutocompleteItem(
            signature = "get(index: int)",
            cursorOffset = 1,
            example = "row.get(3)",
            description = "Given the column index, returns the value of the row at the given index.")
    Object get(int columnIndex);

    @AutocompleteItem(
            signature = "get(columnName: String)",
            cursorOffset = 1,
            example = "row.get('id')",
            description = "Given the column name, returns the value of the row from the given column name.")
    Object get(String columnName);

    @AutocompleteItem(
            signature = "row()",
            example = "row.row()",
            description = "Returns a list containing all the values belonging to this row.")
    List<Object> row();

}
