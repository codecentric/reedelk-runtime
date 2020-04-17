package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.commons.ImmutableMap;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.reedelk.runtime.api.commons.Preconditions.checkNotNull;

public class DefaultDataRow implements DataRow<Serializable> {

    private final Map<String, Serializable> attributes;
    private final List<Serializable> values;
    private final List<String> columns;

    public static DataRow<Serializable> create(List<String> columns, List<Serializable> values) {
        return new DefaultDataRow(columns, values, ImmutableMap.of());
    }

    public static DataRow<Serializable> create(List<String> columns, List<Serializable> values, Map<String, Serializable> attributes) {
        return new DefaultDataRow(columns, values, attributes);
    }

    public DefaultDataRow(List<String> columns, List<Serializable> values, Map<String, Serializable> attributes) {
        checkNotNull(attributes, "attributes");
        checkNotNull(columns, "columns");
        checkNotNull(values, "values");
        this.attributes = Collections.unmodifiableMap(attributes);
        this.columns = Collections.unmodifiableList(columns);
        this.values = Collections.unmodifiableList(values);
    }

    public DefaultDataRow(List<String> columns, List<Serializable> values) {
        this(columns, values, ImmutableMap.of());
    }

    @Override
    public int columnCount() {
        return columns.size();
    }

    @Override
    public String columnName(int columnIndex) {
        return columns.get(columnIndex);
    }

    @Override
    public List<String> columnNames() {
        return columns;
    }

    @Override
    public Serializable get(int columnIndex) {
        return values.get(columnIndex);
    }

    @Override
    public Serializable getByColumnName(String columnName) {
        checkNotNull(columnName, "column name must not be null");
        for (int i = 0; i < columns.size(); i++) {
            if (columnName.equals(columns.get(i))) {
                return values.get(i);
            }
        }
        String error = String.format("A column named=[%s] does not exist.", columnName);
        throw new IllegalStateException(error);
    }

    @Override
    public List<Serializable> values() {
        return values;
    }

    @Override
    public Map<String, Serializable> attributes() {
        return attributes;
    }

    @Override
    public Serializable attribute(String name) {
        return attributes.get(name);
    }
}
