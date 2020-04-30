package com.reedelk.module.descriptor.analyzer;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.resource.ResourceBinary;
import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.api.script.Script;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;


@ModuleComponent("Test Component")
@Description("My test component description")
public class TestComponent implements ProcessorSync {

    @Property("Integer property")
    @InitValue("3")
    private int integerProperty;

    @Property("Integer object property")
    @InitValue("4569")
    private Integer integerObjectProperty;

    @Property("Long property")
    private long longProperty;

    @Property("Long object property")
    private Long longObjectProperty;

    @Property("Float property")
    @InitValue("23.23f")
    private float floatProperty;

    @Property("Float object property")
    @InitValue("13.23f")
    private Float floatObjectProperty;

    @Property("Double property")
    @InitValue("234.5322343d")
    private double doubleProperty;

    @Property("Double object property")
    @InitValue("234.12d")
    private Double doubleObjectProperty;

    @Property("Boolean property")
    @InitValue("true")
    private boolean booleanProperty;

    @Property("Boolean object property")
    @InitValue("true")
    private Boolean booleanObjectProperty;

    @Property("Enum Property")
    @InitValue("VALUE2")
    private TestEnum enumProperty;

    @Property("String property")
    @InitValue("init string value")
    private String stringProperty;

    @Property("Char property")
    @InitValue("c")
    private char charProperty;

    @Property("Char object property")
    @InitValue("b")
    private Character charObjectProperty;

    @Property("Big Integer property")
    @InitValue("6723823")
    private BigInteger bigIntegerProperty;

    @Property("Big Decimal property")
    @InitValue("342.14823")
    private BigDecimal bigDecimalProperty;

    @Property("Resource text property")
    private ResourceText resourceTextProperty;

    @Property("Resource binary property")
    private ResourceBinary resourceBinaryProperty;

    @Property("Resource dynamic property")
    private DynamicResource resourceDynamicProperty;

    // Combo

    @Property("Combo property")
    @InitValue("two")
    @Combo(editable = true, comboValues = {"one", "two", "three"}, prototype = "XXXXXXXXXXXX")
    private String comboProperty;

    // Map

    @Property("Map property")
    @TabGroup("Test tab group")
    private Map<String, String> mapProperty;

    @Property("Map property with init values")
    @TabGroup("Init values tab group")
    @InitValue("{'key1':'value1','key2':'value2'}")
    private Map<String, String> mapPropertyWithInitValues;

    // List

    @Property("List property")
    private List<String> listProperty;

    @Property("List property with init values")
    @InitValue("['one','two','three']")
    private List<String> listPropertyWithInitValues;

    @Property("Script property")
    private Script scriptProperty;

    // Dynamic value types

    @Property("Dynamic big decimal")
    @InitValue("#[56789.234]")
    private DynamicBigDecimal dynamicBigDecimalProperty;

    @Property("Dynamic big integer")
    @InitValue("56789")
    private DynamicBigInteger dynamicBigIntegerProperty;

    @Property("Dynamic boolean")
    @InitValue("true")
    private DynamicBoolean dynamicBooleanProperty;

    @Property("Dynamic byte array")
    @InitValue("#[message.payload()]")
    private DynamicByteArray dynamicByteArrayProperty;

    @Property("Dynamic double")
    @InitValue("234.23d")
    private DynamicDouble dynamicDoubleProperty;

    @Property("Dynamic float")
    @InitValue("#[message.attributes['id']]")
    private DynamicFloat dynamicFloatProperty;

    @Property("Dynamic integer")
    @InitValue("1233")
    private DynamicInteger dynamicIntegerProperty;

    @Property("Dynamic long")
    @InitValue("658291")
    private DynamicLong dynamicLongProperty;

    @Property("Dynamic object")
    @InitValue("my object text")
    private DynamicObject dynamicObjectProperty;

    @Property("Dynamic string")
    private DynamicString dynamicStringProperty;

    // Dynamic map types
    @TabGroup("My dynamic string map")
    @Property("Dynamic string map")
    private DynamicStringMap dynamicStringMapProperty;

    // Special cases
    @Property
    private float withoutDisplayNameProperty;

    @Property("Property with missing init value")
    @InitValue
    private int missingInitValueProperty;

    @Property("Property object with missing init value")
    private Double doubleObjectPropertyWithoutInitValue;

    @Property("Mime type")
    @MimeTypeCombo
    @InitValue(MimeType.AsString.ANY)
    private String mimeType;

    @Property("Mime type with additional types")
    @InitValue("img/xyz")
    @MimeTypeCombo(additionalTypes = "img/xyz,audio/mp13")
    private String mimeTypeCustom;

    @ScriptSignature(arguments = {"arg0", "arg1"}, types = {String.class, Map.class})
    @Property("Property with script variable")
    private DynamicString propertyWithScriptSignature;

    @Property("String property with description text")
    @Description("This is the description text")
    private String stringPropertyWithDescription;

    // Property with example annotation
    @Example("A string example")
    @Property("Property with example")
    private String stringPropertyWithExample;

    // Property with default value annotation
    @DefaultValue("My default value")
    @Property("Property with default value")
    private String stringPropertyWithDefaultValue;


    private int notExposedProperty;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException("not supposed to be called");
    }

    public int getIntegerProperty() {
        return integerProperty;
    }

    public void setIntegerProperty(int integerProperty) {
        this.integerProperty = integerProperty;
    }

    public Integer getIntegerObjectProperty() {
        return integerObjectProperty;
    }

    public void setIntegerObjectProperty(Integer integerObjectProperty) {
        this.integerObjectProperty = integerObjectProperty;
    }

    public long getLongProperty() {
        return longProperty;
    }

    public void setLongProperty(long longProperty) {
        this.longProperty = longProperty;
    }

    public Long getLongObjectProperty() {
        return longObjectProperty;
    }

    public void setLongObjectProperty(Long longObjectProperty) {
        this.longObjectProperty = longObjectProperty;
    }

    public float getFloatProperty() {
        return floatProperty;
    }

    public void setFloatProperty(float floatProperty) {
        this.floatProperty = floatProperty;
    }

    public Float getFloatObjectProperty() {
        return floatObjectProperty;
    }

    public void setFloatObjectProperty(Float floatObjectProperty) {
        this.floatObjectProperty = floatObjectProperty;
    }

    public double getDoubleProperty() {
        return doubleProperty;
    }

    public void setDoubleProperty(double doubleProperty) {
        this.doubleProperty = doubleProperty;
    }

    public Double getDoubleObjectProperty() {
        return doubleObjectProperty;
    }

    public void setDoubleObjectProperty(Double doubleObjectProperty) {
        this.doubleObjectProperty = doubleObjectProperty;
    }

    public boolean isBooleanProperty() {
        return booleanProperty;
    }

    public void setBooleanProperty(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    public Boolean getBooleanObjectProperty() {
        return booleanObjectProperty;
    }

    public void setBooleanObjectProperty(Boolean booleanObjectProperty) {
        this.booleanObjectProperty = booleanObjectProperty;
    }

    public TestEnum getEnumProperty() {
        return enumProperty;
    }

    public void setEnumProperty(TestEnum enumProperty) {
        this.enumProperty = enumProperty;
    }

    public String getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public BigInteger getBigIntegerProperty() {
        return bigIntegerProperty;
    }

    public void setBigIntegerProperty(BigInteger bigIntegerProperty) {
        this.bigIntegerProperty = bigIntegerProperty;
    }

    public BigDecimal getBigDecimalProperty() {
        return bigDecimalProperty;
    }

    public void setBigDecimalProperty(BigDecimal bigDecimalProperty) {
        this.bigDecimalProperty = bigDecimalProperty;
    }

    public ResourceText getResourceTextProperty() {
        return resourceTextProperty;
    }

    public void setResourceTextProperty(ResourceText resourceTextProperty) {
        this.resourceTextProperty = resourceTextProperty;
    }

    public ResourceBinary getResourceBinaryProperty() {
        return resourceBinaryProperty;
    }

    public void setResourceBinaryProperty(ResourceBinary resourceBinaryProperty) {
        this.resourceBinaryProperty = resourceBinaryProperty;
    }

    public DynamicResource getResourceDynamicProperty() {
        return resourceDynamicProperty;
    }

    public void setResourceDynamicProperty(DynamicResource resourceDynamicProperty) {
        this.resourceDynamicProperty = resourceDynamicProperty;
    }

    public String getComboProperty() {
        return comboProperty;
    }

    public void setComboProperty(String comboProperty) {
        this.comboProperty = comboProperty;
    }

    public Map<String, String> getMapProperty() {
        return mapProperty;
    }

    public void setMapProperty(Map<String, String> mapProperty) {
        this.mapProperty = mapProperty;
    }

    public Map<String, String> getMapPropertyWithInitValues() {
        return mapPropertyWithInitValues;
    }

    public void setMapPropertyWithInitValues(Map<String, String> mapPropertyWithInitValues) {
        this.mapPropertyWithInitValues = mapPropertyWithInitValues;
    }

    public Script getScriptProperty() {
        return scriptProperty;
    }

    public void setScriptProperty(Script scriptProperty) {
        this.scriptProperty = scriptProperty;
    }

    public DynamicBigDecimal getDynamicBigDecimalProperty() {
        return dynamicBigDecimalProperty;
    }

    public void setDynamicBigDecimalProperty(DynamicBigDecimal dynamicBigDecimalProperty) {
        this.dynamicBigDecimalProperty = dynamicBigDecimalProperty;
    }

    public DynamicBigInteger getDynamicBigIntegerProperty() {
        return dynamicBigIntegerProperty;
    }

    public void setDynamicBigIntegerProperty(DynamicBigInteger dynamicBigIntegerProperty) {
        this.dynamicBigIntegerProperty = dynamicBigIntegerProperty;
    }

    public DynamicBoolean getDynamicBooleanProperty() {
        return dynamicBooleanProperty;
    }

    public void setDynamicBooleanProperty(DynamicBoolean dynamicBooleanProperty) {
        this.dynamicBooleanProperty = dynamicBooleanProperty;
    }

    public DynamicByteArray getDynamicByteArrayProperty() {
        return dynamicByteArrayProperty;
    }

    public void setDynamicByteArrayProperty(DynamicByteArray dynamicByteArrayProperty) {
        this.dynamicByteArrayProperty = dynamicByteArrayProperty;
    }

    public DynamicDouble getDynamicDoubleProperty() {
        return dynamicDoubleProperty;
    }

    public void setDynamicDoubleProperty(DynamicDouble dynamicDoubleProperty) {
        this.dynamicDoubleProperty = dynamicDoubleProperty;
    }

    public DynamicFloat getDynamicFloatProperty() {
        return dynamicFloatProperty;
    }

    public void setDynamicFloatProperty(DynamicFloat dynamicFloatProperty) {
        this.dynamicFloatProperty = dynamicFloatProperty;
    }

    public DynamicInteger getDynamicIntegerProperty() {
        return dynamicIntegerProperty;
    }

    public void setDynamicIntegerProperty(DynamicInteger dynamicIntegerProperty) {
        this.dynamicIntegerProperty = dynamicIntegerProperty;
    }

    public DynamicLong getDynamicLongProperty() {
        return dynamicLongProperty;
    }

    public void setDynamicLongProperty(DynamicLong dynamicLongProperty) {
        this.dynamicLongProperty = dynamicLongProperty;
    }

    public DynamicObject getDynamicObjectProperty() {
        return dynamicObjectProperty;
    }

    public void setDynamicObjectProperty(DynamicObject dynamicObjectProperty) {
        this.dynamicObjectProperty = dynamicObjectProperty;
    }

    public DynamicString getDynamicStringProperty() {
        return dynamicStringProperty;
    }

    public void setDynamicStringProperty(DynamicString dynamicStringProperty) {
        this.dynamicStringProperty = dynamicStringProperty;
    }

    public DynamicStringMap getDynamicStringMapProperty() {
        return dynamicStringMapProperty;
    }

    public void setDynamicStringMapProperty(DynamicStringMap dynamicStringMapProperty) {
        this.dynamicStringMapProperty = dynamicStringMapProperty;
    }

    public float getWithoutDisplayNameProperty() {
        return withoutDisplayNameProperty;
    }

    public void setWithoutDisplayNameProperty(float withoutDisplayNameProperty) {
        this.withoutDisplayNameProperty = withoutDisplayNameProperty;
    }

    public int getMissingInitValueProperty() {
        return missingInitValueProperty;
    }

    public void setMissingInitValueProperty(int missingInitValueProperty) {
        this.missingInitValueProperty = missingInitValueProperty;
    }

    public Double getDoubleObjectPropertyWithoutInitValue() {
        return doubleObjectPropertyWithoutInitValue;
    }

    public void setDoubleObjectPropertyWithoutInitValue(Double doubleObjectPropertyWithoutInitValue) {
        this.doubleObjectPropertyWithoutInitValue = doubleObjectPropertyWithoutInitValue;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeTypeCustom() {
        return mimeTypeCustom;
    }

    public void setMimeTypeCustom(String mimeTypeCustom) {
        this.mimeTypeCustom = mimeTypeCustom;
    }

    public DynamicString getPropertyWithScriptSignature() {
        return propertyWithScriptSignature;
    }

    public void setPropertyWithScriptSignature(DynamicString propertyWithScriptSignature) {
        this.propertyWithScriptSignature = propertyWithScriptSignature;
    }

    public String getStringPropertyWithDescription() {
        return stringPropertyWithDescription;
    }

    public void setStringPropertyWithDescription(String stringPropertyWithDescription) {
        this.stringPropertyWithDescription = stringPropertyWithDescription;
    }

    public char getCharProperty() {
        return charProperty;
    }

    public void setCharProperty(char charProperty) {
        this.charProperty = charProperty;
    }

    public Character getCharObjectProperty() {
        return charObjectProperty;
    }

    public void setCharObjectProperty(Character charObjectProperty) {
        this.charObjectProperty = charObjectProperty;
    }
}

