package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            signature = "getValues()",
            example = "row.getValues()",
            description = "Returns a list containing all the values belonging to this row.")
    default List<T> getValues() {
        return values();
    }

    @AutocompleteItem(
            signature = "values()",
            example = "row.values()",
            description = "Returns a list containing all the values belonging to this row.")
    List<T> values();

    @AutocompleteItem(
            signature = "getAttributes()",
            example = "row.getAttributes()",
            description = "Returns the attributes of this row such as the column types for a Database data row.")
    default Map<String, Serializable> getAttributes() {
        return attributes();
    }

    @AutocompleteItem(
            signature = "attributes()",
            example = "row.attributes()",
            description = "Returns the attributes of this row such as the column types for a Database data row.")
    Map<String, Serializable> attributes();

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "getAttribute(attributeName: String)",
            example = "row.getAttribute('columnTypes')",
            description = "Returns a single attribute of this row such as the column types for a Database data row.")
    default Serializable getAttribute(String name) {
        return attribute(name);
    }

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "attribute(attributeName: String)",
            example = "row.attribute('columnTypes')",
            description = "Returns a single attribute of this row such as the column types for a Database data row.")
    Serializable attribute(String name);

    @AutocompleteItem(
            signature = "asMap()",
            example = "row.asMap()",
            description = "Returns this data row as map. " +
                    "The map keys are the column names and map values are the values corresponding " +
                    "to the mapped column name.")
    default Map<String, T> asMap() {
        List<String> columnNames = getColumnNames();
        Map<String, T> out = new HashMap<>();
        for (String columnName : columnNames) {
            T value = getByColumnName(columnName);
            out.put(columnName, value);
        }
        return out;
    }
}
