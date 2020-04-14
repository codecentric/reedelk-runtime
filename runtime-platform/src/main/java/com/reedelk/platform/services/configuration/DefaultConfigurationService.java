package com.reedelk.platform.services.configuration;

import com.reedelk.platform.commons.Messages.ConfigProperty;
import com.reedelk.platform.services.configuration.configurer.*;
import com.reedelk.platform.services.configuration.converter.*;
import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.api.exception.ConfigPropertyException;
import com.reedelk.runtime.api.script.dynamicvalue.*;
import com.reedelk.runtime.system.api.SystemProperty;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.list;
import static java.util.stream.Collectors.toList;

public class DefaultConfigurationService implements ConfigurationService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultConfigurationService.class);
    private static final List<Configurer> CONFIGURERS = asList(new LogbackConfigurer(), new PidConfigConfigurer());
    private static final String DEFAULT_CONFIG_FILE_PID = "configuration";

    private final ConfigurationAdmin configurationAdmin;
    private final SystemProperty systemProperty;

    public DefaultConfigurationService(ConfigurationAdmin configurationAdmin, SystemProperty systemProperty) {
        this.configurationAdmin = configurationAdmin;
        this.systemProperty = systemProperty;
    }

    // String

    @Override
    public String getStringFrom(String configPID, String configKey, String defaultValue) {
        return Optional.ofNullable(getStringSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminProperty(configPID, configKey, defaultValue, TO_STRING));
    }

    @Override
    public String getStringFrom(String configPID, String configKey) {
        return Optional.ofNullable(getStringSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminPropertyOrThrow(configPID, configKey, TO_STRING));
    }

    @Override
    public String getString(String configKey, String defaultValue) {
        return getStringFrom(DEFAULT_CONFIG_FILE_PID, configKey, defaultValue);
    }

    @Override
    public String getString(String configKey) {
        return getStringFrom(DEFAULT_CONFIG_FILE_PID, configKey);
    }

    // Integer

    @Override
    public int getIntFrom(String configPID, String configKey, int defaultValue) {
        return Optional.ofNullable(getIntSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminProperty(configPID, configKey, defaultValue, TO_INT));
    }

    @Override
    public int getIntFrom(String configPID, String configKey) {
        return Optional.ofNullable(getIntSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminPropertyOrThrow(configPID, configKey, TO_INT));
    }

    @Override
    public int getInt(String configKey, int defaultValue) {
        return getIntFrom(DEFAULT_CONFIG_FILE_PID, configKey, defaultValue);
    }

    @Override
    public int getInt(String configKey) {
        return getIntFrom(DEFAULT_CONFIG_FILE_PID,configKey);
    }

    // Long

    @Override
    public long getLongFrom(String configPID, String configKey, long defaultValue) {
        return Optional.ofNullable(getLongSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminProperty(configPID, configKey, defaultValue, TO_LONG));
    }

    @Override
    public long getLongFrom(String configPID, String configKey) {
        return Optional.ofNullable(getLongSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminPropertyOrThrow(configPID, configKey, TO_LONG));
    }

    @Override
    public long getLong(String configKey, long defaultValue) {
        return getLongFrom(DEFAULT_CONFIG_FILE_PID, configKey, defaultValue);
    }

    @Override
    public long getLong(String configKey) {
        return getLongFrom(DEFAULT_CONFIG_FILE_PID, configKey);
    }

    // Double

    @Override
    public double getDoubleFrom(String configPID, String configKey, double defaultValue) {
        return Optional.ofNullable(getDoubleSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminProperty(configPID, configKey, defaultValue, TO_DOUBLE));
    }

    @Override
    public double getDoubleFrom(String configPID, String configKey) {
        return Optional.ofNullable(getDoubleSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminPropertyOrThrow(configPID, configKey, TO_DOUBLE));
    }

    @Override
    public double getDouble(String configKey, double defaultValue) {
        return getDoubleFrom(DEFAULT_CONFIG_FILE_PID, configKey, defaultValue);
    }

    @Override
    public double getDouble(String configKey) {
        return getDoubleFrom(DEFAULT_CONFIG_FILE_PID, configKey);
    }

    // Float

    @Override
    public float getFloatFrom(String configPID, String configKey, float defaultValue) {
        return Optional.ofNullable(getFloatSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminProperty(configPID, configKey, defaultValue, TO_FLOAT));
    }

    @Override
    public float getFloatFrom(String configPID, String configKey) {
        return Optional.ofNullable(getFloatSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminPropertyOrThrow(configPID, configKey, TO_FLOAT));
    }

    @Override
    public float getFloat(String configKey, float defaultValue) {
        return getFloatFrom(DEFAULT_CONFIG_FILE_PID, configKey, defaultValue);
    }

    @Override
    public float getFloat(String configKey) {
        return getFloatFrom(DEFAULT_CONFIG_FILE_PID, configKey);
    }

    // Boolean

    @Override
    public boolean getBooleanFrom(String configPID, String configKey, boolean defaultValue) {
        return Optional.ofNullable(getBooleanSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminProperty(configPID, configKey, defaultValue, TO_BOOLEAN));
    }

    @Override
    public boolean getBooleanFrom(String configPID, String configKey) {
        return Optional.ofNullable(getBooleanSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminPropertyOrThrow(configPID, configKey, TO_BOOLEAN));
    }

    @Override
    public boolean getBoolean(String configKey, boolean defaultValue) {
        return getBooleanFrom(DEFAULT_CONFIG_FILE_PID, configKey, defaultValue);
    }

    @Override
    public boolean getBoolean(String configKey) {
        return getBooleanFrom(DEFAULT_CONFIG_FILE_PID, configKey);
    }

    // BigDecimal

    @Override
    public BigDecimal getBigDecimalFrom(String configPID, String configKey, BigDecimal defaultValue) {
        return Optional.ofNullable(getBigDecimalSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminProperty(configPID, configKey, defaultValue, TO_BIG_DECIMAL));
    }

    @Override
    public BigDecimal getBigDecimalFrom(String configPID, String configKey) {
        return Optional.ofNullable(getBigDecimalSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminPropertyOrThrow(configPID, configKey, TO_BIG_DECIMAL));
    }

    @Override
    public BigDecimal getBigDecimal(String configKey, BigDecimal defaultValue) {
        return getBigDecimalFrom(DEFAULT_CONFIG_FILE_PID, configKey, defaultValue);
    }

    @Override
    public BigDecimal getBigDecimal(String configKey) {
        return getBigDecimalFrom(DEFAULT_CONFIG_FILE_PID, configKey);
    }

    // BigInteger

    @Override
    public BigInteger getBigIntegerFrom(String configPID, String configKey, BigInteger defaultValue) {
        return Optional.ofNullable(getBigIntegerSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminProperty(configPID, configKey, defaultValue, TO_BIG_INTEGER));
    }

    @Override
    public BigInteger getBigIntegerFrom(String configPID, String configKey) {
        return Optional.ofNullable(getBigIntegerSystemProperty(configKey))
                .orElseGet(() -> getConfigAdminPropertyOrThrow(configPID, configKey, TO_BIG_INTEGER));
    }

    @Override
    public BigInteger getBigInteger(String configKey, BigInteger defaultValue) {
        return getBigIntegerFrom(DEFAULT_CONFIG_FILE_PID, configKey, defaultValue);
    }

    @Override
    public BigInteger getBigInteger(String configKey) {
        return getBigIntegerFrom(DEFAULT_CONFIG_FILE_PID, configKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getFrom(String configPID, String configKey, T defaultValue, Class<T> type) {
        if (MAP.containsKey(type)) {
            return (T) MAP.get(type).convert(this, configPID, configKey, defaultValue);
        }
        throw new ConfigPropertyException(ConfigProperty.UNSUPPORTED_CONVERSION.format(configKey, configPID, type.getName()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getFrom(String configPID, String configKey, Class<T> type) {
        if (MAP.containsKey(type)) {
            return (T) MAP.get(type).convert(this, configPID, configKey);
        }
        throw new ConfigPropertyException(ConfigProperty.UNSUPPORTED_CONVERSION.format(configKey, configPID, type.getName()));
    }

    @Override
    public <T> T get(String configKey, T defaultValue, Class<T> type) {
        return getFrom(DEFAULT_CONFIG_FILE_PID, configKey, defaultValue, type);
    }

    @Override
    public <T> T get(String configKey, Class<T> type) {
        return getFrom(DEFAULT_CONFIG_FILE_PID, configKey, type);
    }

    /**
     * Applies a given Configurer object to each config file found in the config directory.
     * For a given file type (e.g .property) there might be multiple configurers.
     */
    public void initialize() {
        listConfigFilesFromConfigDirectory()
                .forEach(configFile -> {
                    for (Configurer configurer : CONFIGURERS) {
                        // We break loop if configuration has been applied.
                        if (configurer.apply(configurationAdmin, configFile)) break;
                    }
                });
    }

    Integer getIntSystemProperty(String key) {
        return Optional.ofNullable(getStringSystemProperty(key))
                .map(Integer::valueOf)
                .orElse(null);
    }

    Long getLongSystemProperty(String key) {
        return Optional.ofNullable(getStringSystemProperty(key))
                .map(Long::valueOf)
                .orElse(null);
    }

    Double getDoubleSystemProperty(String key) {
        return Optional.ofNullable(getStringSystemProperty(key))
                .map(Double::valueOf)
                .orElse(null);
    }

    Float getFloatSystemProperty(String key) {
        return Optional.ofNullable(getStringSystemProperty(key))
                .map(Float::valueOf)
                .orElse(null);
    }

    BigDecimal getBigDecimalSystemProperty(String key) {
        return Optional.ofNullable(getStringSystemProperty(key))
                .map(BigDecimal::new)
                .orElse(null);
    }

    BigInteger getBigIntegerSystemProperty(String key) {
        return Optional.ofNullable(getStringSystemProperty(key))
                .map(BigInteger::new)
                .orElse(null);
    }

    Boolean getBooleanSystemProperty(String key) {
        return Optional.ofNullable(getStringSystemProperty(key))
                .map(Boolean::valueOf)
                .orElse(null);
    }

    String getStringSystemProperty(String key) {
        return System.getProperty(key);
    }

    Collection<ConfigFile> listConfigFilesFromConfigDirectory() {
        File configDirectory = new File(systemProperty.configDirectory());
        File[] files = configDirectory.listFiles(File::isFile);
        if (files == null) return emptyList();
        return stream(files)
                .map(ConfigFileFactory::get)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    <T> T getConfigAdminProperty(String configPid, String configKey, T defaultValue, InputMapper<T> mapper) {
        try {
            Configuration configuration = configurationAdmin.getConfiguration(configPid);
            Dictionary<String, Object> properties = configuration.getProperties();
            if (properties == null) return defaultValue;
            return getPropertyOrDefault(properties, configKey, defaultValue, mapper);
        } catch (IOException e) {
            logger.warn(ConfigProperty.NOT_FOUND_WITH_KEY_AND_PID_AND_DEFAULT.format(configKey, configPid, defaultValue));
            return defaultValue;
        }
    }

    <T> T getConfigAdminPropertyOrThrow(String configPid, String configKey, InputMapper<T> mapper) {
        try {
            Configuration configuration = configurationAdmin.getConfiguration(configPid);
            Dictionary<String, Object> properties = configuration.getProperties();
            return getPropertyOrThrow(properties, configKey, mapper);
        } catch (IOException e) {
            throw new ConfigPropertyException(ConfigProperty.NOT_FOUND_WITH_KEY_AND_PID.format(configKey, configPid));
        }
    }

    private <T> T getPropertyOrThrow(Dictionary<String, Object> dictionary, String configKey, InputMapper<T> mapper) {
        if (dictionary != null && list(dictionary.keys()).contains(configKey)) {
            return mapper.map(dictionary.get(configKey));
        }
        throw new ConfigPropertyException(ConfigProperty.NOT_FOUND_WITH_KEY.format(configKey));
    }

    private <T> T getPropertyOrDefault(Dictionary<String, Object> dictionary, String configKey, T defaultValue, InputMapper<T> mapper) {
        return list(dictionary.keys()).contains(configKey) ?
                mapper.map(dictionary.get(configKey)) :
                defaultValue;
    }

    private static final Map<Class, ConfigConverter> MAP;
    static {
        Map<Class, ConfigConverter> tmp = new HashMap<>();

        // primitives

        tmp.put(String.class, new StringConfigConverter());
        tmp.put(boolean.class, new BooleanConfigConverter());
        tmp.put(Boolean.class, new BooleanConfigConverter());
        tmp.put(int.class, new IntegerConfigConverter());
        tmp.put(Integer.class, new IntegerConfigConverter());
        tmp.put(long.class, new LongConfigConverter());
        tmp.put(Long.class, new LongConfigConverter());
        tmp.put(double.class, new DoubleConfigConverter());
        tmp.put(Double.class, new DoubleConfigConverter());
        tmp.put(float.class, new FloatConfigConverter());
        tmp.put(Float.class, new FloatConfigConverter());
        tmp.put(BigDecimal.class, new BigDecimalConfigConverter());
        tmp.put(BigInteger.class, new BigIntegerConfigConverter());

        // dynamic types

        tmp.put(DynamicString.class, new DynamicStringConfigConverter());
        tmp.put(DynamicBoolean.class, new DynamicBooleanConfigConverter());
        tmp.put(DynamicInteger.class, new DynamicIntegerConfigConverter());
        tmp.put(DynamicLong.class, new DynamicLongConfigConverter());
        tmp.put(DynamicDouble.class, new DynamicDoubleConfigConverter());
        tmp.put(DynamicFloat.class, new DynamicFloatConfigConverter());
        tmp.put(DynamicBigDecimal.class, new DynamicBigDecimalConfigConverter());
        tmp.put(DynamicBigInteger.class, new DynamicBigIntegerConfigConverter());

        MAP = Collections.unmodifiableMap(tmp);
    }

    private static final InputMapper<String> TO_STRING = input -> (String) input;
    private static final InputMapper<Boolean> TO_BOOLEAN = input -> input instanceof String ? Boolean.valueOf((String) input) : (Boolean) input;
    private static final InputMapper<Integer> TO_INT = input -> input instanceof String ? Integer.valueOf((String) input) : (Integer) input;
    private static final InputMapper<Long> TO_LONG = input -> input instanceof String ? Long.valueOf((String) input) : (Long) input;
    private static final InputMapper<Double> TO_DOUBLE = input -> input instanceof String ? Double.valueOf((String) input) : (Double) input;
    private static final InputMapper<Float> TO_FLOAT = input -> input instanceof String ? Float.valueOf((String) input) : (Float) input;
    private static final InputMapper<BigDecimal> TO_BIG_DECIMAL = input -> input instanceof String ? new BigDecimal((String) input) : (BigDecimal) input;
    private static final InputMapper<BigInteger> TO_BIG_INTEGER = input -> input instanceof String ? new BigInteger((String) input) : (BigInteger) input;

    interface InputMapper<T> {
        T map(Object input);
    }
}
