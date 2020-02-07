package com.reedelk.runtime.api.message.content;

import java.util.List;

public interface ResultRow {

    int getColumnCount();

    String getColumnName(int i);

    List<String> columnNames();

    Object get(int column);

    List<Object> row();

}