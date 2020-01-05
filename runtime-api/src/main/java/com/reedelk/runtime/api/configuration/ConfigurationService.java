package com.reedelk.runtime.api.configuration;

import com.reedelk.runtime.api.exception.ConfigPropertyException;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface ConfigurationService {

    // String

    String getStringFrom(String configPID, String configKey, String defaultValue);

    String getStringFrom(String configPID, String configKey) throws ConfigPropertyException;

    String getString(String configKey, String defaultValue);

    String getString(String configKey) throws ConfigPropertyException;

    // Int

    int getIntFrom(String configPID, String configKey, int defaultValue);

    int getIntFrom(String configPID, String configKey) throws ConfigPropertyException;

    int getInt(String configKey, int defaultValue);

    int getInt(String configKey) throws ConfigPropertyException;

    // Long

    long getLongFrom(String configPID, String configKey, long defaultValue);

    long getLongFrom(String configPID, String configKey) throws ConfigPropertyException;

    long getLong(String configKey, long defaultValue);

    long getLong(String configKey) throws ConfigPropertyException;

    // Double

    double getDoubleFrom(String configPID, String configKey, double defaultValue);

    double getDoubleFrom(String configPID, String configKey) throws ConfigPropertyException;

    double getDouble(String configKey, double defaultValue);

    double getDouble(String configKey) throws ConfigPropertyException;

    // Float

    float getFloatFrom(String configPID, String configKey, float defaultValue);

    float getFloatFrom(String configPID, String configKey) throws ConfigPropertyException;

    float getFloat(String configKey, float defaultValue);

    float getFloat(String configKey) throws ConfigPropertyException;

    // Boolean

    boolean getBooleanFrom(String configPID, String configKey, boolean defaultValue);

    boolean getBooleanFrom(String configPID, String configKey) throws ConfigPropertyException;

    boolean getBoolean(String configKey, boolean defaultValue);

    boolean getBoolean(String configKey) throws ConfigPropertyException;

    // BigDecimal

    BigDecimal getBigDecimalFrom(String configPID, String configKey, BigDecimal defaultValue);

    BigDecimal getBigDecimalFrom(String configPID, String configKey) throws ConfigPropertyException;

    BigDecimal getBigDecimal(String configKey, BigDecimal defaultValue);

    BigDecimal getBigDecimal(String configKey) throws ConfigPropertyException;

    // BigInteger

    BigInteger getBigIntegerFrom(String configPID, String configKey, BigInteger defaultValue);

    BigInteger getBigIntegerFrom(String configPID, String configKey) throws ConfigPropertyException;

    BigInteger getBigInteger(String configKey, BigInteger defaultValue);

    BigInteger getBigInteger(String configKey) throws ConfigPropertyException;

    // Any

    <T> T getFrom(String configPID, String configKey, T defaultValue, Class<T> type);

    <T> T getFrom(String configPID, String configKey, Class<T> type) throws ConfigPropertyException;

    <T> T get(String configKey, T defaultValue, Class<T> type);

    <T> T get(String configKey, Class<T> type) throws ConfigPropertyException;

}
