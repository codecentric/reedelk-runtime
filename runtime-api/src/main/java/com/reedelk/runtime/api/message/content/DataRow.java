package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.util.List;

@AutocompleteType(description = "The DataRow type encapsulates a generic data row. " +
        "Such as a database row, a csv document row and so on.")
public interface DataRow<T extends Serializable> extends Serializable {

    @AutocompleteItem(
            signature = "columnCount()",
            example = "row.columnCount()",
            description = "Returns the number of columns in this row.")
    int columnCount();

    @AutocompleteItem(
            signature = "getColumnCount()",
            example = "row.getColumnCount()",
            description = "Returns the number of columns in this row.")
    default int getColumnCount() {
        return columnCount();
    }

    @AutocompleteItem(
            signature = "columnName(index: int)",
            cursorOffset = 1,
            example = "row.columnName(4)",
            description = "Given the column index, returns the column name at the given index.")
    String columnName(int columnIndex);

    @AutocompleteItem(
            signature = "getColumnName(index: int)",
            cursorOffset = 1,
            example = "row.getColumnName(4)",
            description = "Given the column index, returns the column name at the given index.")
    default String getColumnName(int columnIndex) {
        return columnName(columnIndex);
    }

    @AutocompleteItem(
            signature = "columnNames()",
            example = "row.columnNames()",
            description = "Returns a list containing all column names of this data row.")
    List<String> columnNames();

    @AutocompleteItem(
            signature = "getColumnNames()",
            example = "row.getColumnNames()",
            description = "Returns a list containing all column names of this data row.")
    default List<String> getColumnNames() {
        return columnNames();
    }

    @AutocompleteItem(
            signature = "get(index: int)",
            cursorOffset = 1,
            example = "row.get(3)",
            description = "Given the column index, returns the value of the row at the given index.")
    T get(int columnIndex);

    @AutocompleteItem(
            signature = "getByName(columnName: String)",
            cursorOffset = 1,
            example = "row.getByName('id')",
            description = "Given the column name, returns the value of the row from the given column name.")
    T getByColumnName(String columnName);

    @AutocompleteItem(
            signature = "row()",
            example = "row.row()",
            description = "Returns a list containing all the values belonging to this row.")
    List<T> row();

}
