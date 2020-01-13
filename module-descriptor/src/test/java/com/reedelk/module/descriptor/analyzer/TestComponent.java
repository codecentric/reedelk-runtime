package com.reedelk.module.descriptor.analyzer;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.message.FlowContext;
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
import java.util.Map;


@ESBComponent("Test Component")
public class TestComponent implements ProcessorSync {

    @Property("Integer property")
    @Default("3")
    private int integerProperty;

    @Property("Integer object property")
    @Default("4569")
    private Integer integerObjectProperty;

    @Property("Long property")
    private long longProperty;

    @Property("Long object property")
    private Long longObjectProperty;

    @Property("Float property")
    @Default("23.23f")
    private float floatProperty;

    @Property("Float object property")
    @Default("13.23f")
    private Float floatObjectProperty;

    @Property("Double property")
    @Default("234.5322343d")
    private double doubleProperty;

    @Property("Double object property")
    @Default("234.12d")
    private Double doubleObjectProperty;

    @Property("Boolean property")
    @Default("true")
    private boolean booleanProperty;

    @Property("Boolean object property")
    @Default("true")
    private Boolean booleanObjectProperty;

    @Property("Enum Property")
    @Default("VALUE2")
    private TestEnum enumProperty;

    @Property("String property")
    @Default("default string value")
    private String stringProperty;

    @Property("Big Integer property")
    @Default("6723823")
    private BigInteger bigIntegerProperty;

    @Property("Big Decimal property")
    @Default("342.14823")
    private BigDecimal bigDecimalProperty;

    @Property("Resource text property")
    private ResourceText resourceTextProperty;

    @Property("Resource binary property")
    private ResourceBinary resourceBinaryProperty;

    @Property("Resource dynamic property")
    private DynamicResource resourceDynamicProperty;

    @Property("Combo property")
    @Default("two")
    @Combo(editable = true, comboValues = {"one", "two", "three"}, prototype = "XXXXXXXXXXXX")
    private String comboProperty;

    @Property("Map property")
    @TabGroup("Test tab group")
    private Map<String, String> mapProperty;

    @Property("Map property with defaults")
    @TabGroup("Default values tab group")
    @Default("{'key1':'value1','key2':'value2'}")
    private Map<String, String> mapPropertyWithDefaultValues;

    @Property("Script property")
    private Script scriptProperty;

    // Dynamic value types

    @Property("Dynamic big decimal")
    @Default("#[56789.234]")
    private DynamicBigDecimal dynamicBigDecimalProperty;

    @Property("Dynamic big integer")
    @Default("56789")
    private DynamicBigInteger dynamicBigIntegerProperty;

    @Property("Dynamic boolean")
    @Default("true")
    private DynamicBoolean dynamicBooleanProperty;

    @Property("Dynamic byte array")
    @Default("#[message.payload()]")
    private DynamicByteArray dynamicByteArrayProperty;

    @Property("Dynamic double")
    @Default("234.23d")
    private DynamicDouble dynamicDoubleProperty;

    @Property("Dynamic float")
    @Default("#[message.attributes['id']]")
    private DynamicFloat dynamicFloatProperty;

    @Property("Dynamic integer")
    @Default("1233")
    private DynamicInteger dynamicIntegerProperty;

    @Property("Dynamic long")
    @Default("658291")
    private DynamicLong dynamicLongProperty;

    @Property("Dynamic object")
    @Default("my object text")
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

    @Property("Property with missing default value")
    @Default
    private int missingDefaultValueProperty;

    @Property("Property object with missing default value")
    private Double doubleObjectPropertyWithoutDefaultValue;

    @Property("Mime type")
    @MimeTypeCombo
    @Default(MimeType.MIME_TYPE_ANY)
    private String mimeType;

    @Property("Mime type with additional types")
    @Default("img/xyz")
    @MimeTypeCombo(additionalTypes = "img/xyz,audio/mp13")
    private String mimeTypeCustom;

    @AutoCompleteContributor(message = false)
    @Property("Property with autocomplete contributor")
    private DynamicString propertyWithAutoCompleteContributor;

    @Property("String property with info text")
    @PropertyInfo("This is the info text")
    private String stringPropertyWithTooltipInfo;


    private int notExposedProperty;

    @Override
    public Message apply(Message message, FlowContext flowContext) {
        throw new UnsupportedOperationException("not supposed to be called");
    }

    public void setIntegerProperty(int integerProperty) {
        this.integerProperty = integerProperty;
    }

    public void setIntegerObjectProperty(Integer integerObjectProperty) {
        this.integerObjectProperty = integerObjectProperty;
    }

    public void setLongProperty(long longProperty) {
        this.longProperty = longProperty;
    }

    public void setLongObjectProperty(Long longObjectProperty) {
        this.longObjectProperty = longObjectProperty;
    }

    public void setFloatProperty(float floatProperty) {
        this.floatProperty = floatProperty;
    }

    public void setFloatObjectProperty(Float floatObjectProperty) {
        this.floatObjectProperty = floatObjectProperty;
    }

    public void setDoubleProperty(double doubleProperty) {
        this.doubleProperty = doubleProperty;
    }

    public void setDoubleObjectProperty(Double doubleObjectProperty) {
        this.doubleObjectProperty = doubleObjectProperty;
    }

    public void setBooleanProperty(boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    public void setBooleanObjectProperty(Boolean booleanObjectProperty) {
        this.booleanObjectProperty = booleanObjectProperty;
    }

    public void setEnumProperty(TestEnum enumProperty) {
        this.enumProperty = enumProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public void setBigIntegerProperty(BigInteger bigIntegerProperty) {
        this.bigIntegerProperty = bigIntegerProperty;
    }

    public void setBigDecimalProperty(BigDecimal bigDecimalProperty) {
        this.bigDecimalProperty = bigDecimalProperty;
    }

    public void setResourceTextProperty(ResourceText resourceTextProperty) {
        this.resourceTextProperty = resourceTextProperty;
    }

    public void setResourceBinaryProperty(ResourceBinary resourceBinaryProperty) {
        this.resourceBinaryProperty = resourceBinaryProperty;
    }

    public void setResourceDynamicProperty(DynamicResource resourceDynamicProperty) {
        this.resourceDynamicProperty = resourceDynamicProperty;
    }

    public void setComboProperty(String comboProperty) {
        this.comboProperty = comboProperty;
    }

    public void setMapProperty(Map<String, String> mapProperty) {
        this.mapProperty = mapProperty;
    }

    public void setScriptProperty(Script scriptProperty) {
        this.scriptProperty = scriptProperty;
    }

    public void setDynamicBigDecimalProperty(DynamicBigDecimal dynamicBigDecimalProperty) {
        this.dynamicBigDecimalProperty = dynamicBigDecimalProperty;
    }

    public void setDynamicBigIntegerProperty(DynamicBigInteger dynamicBigIntegerProperty) {
        this.dynamicBigIntegerProperty = dynamicBigIntegerProperty;
    }

    public void setDynamicBooleanProperty(DynamicBoolean dynamicBooleanProperty) {
        this.dynamicBooleanProperty = dynamicBooleanProperty;
    }

    public void setDynamicByteArrayProperty(DynamicByteArray dynamicByteArrayProperty) {
        this.dynamicByteArrayProperty = dynamicByteArrayProperty;
    }

    public void setDynamicDoubleProperty(DynamicDouble dynamicDoubleProperty) {
        this.dynamicDoubleProperty = dynamicDoubleProperty;
    }

    public void setDynamicFloatProperty(DynamicFloat dynamicFloatProperty) {
        this.dynamicFloatProperty = dynamicFloatProperty;
    }

    public void setDynamicIntegerProperty(DynamicInteger dynamicIntegerProperty) {
        this.dynamicIntegerProperty = dynamicIntegerProperty;
    }

    public void setDynamicLongProperty(DynamicLong dynamicLongProperty) {
        this.dynamicLongProperty = dynamicLongProperty;
    }

    public void setDynamicObjectProperty(DynamicObject dynamicObjectProperty) {
        this.dynamicObjectProperty = dynamicObjectProperty;
    }

    public void setDynamicStringProperty(DynamicString dynamicStringProperty) {
        this.dynamicStringProperty = dynamicStringProperty;
    }

    public void setDynamicStringMapProperty(DynamicStringMap dynamicStringMapProperty) {
        this.dynamicStringMapProperty = dynamicStringMapProperty;
    }

    public void setWithoutDisplayNameProperty(float withoutDisplayNameProperty) {
        this.withoutDisplayNameProperty = withoutDisplayNameProperty;
    }

    public void setMissingDefaultValueProperty(int missingDefaultValueProperty) {
        this.missingDefaultValueProperty = missingDefaultValueProperty;
    }

    public void setDoubleObjectPropertyWithoutDefaultValue(Double doubleObjectPropertyWithoutDefaultValue) {
        this.doubleObjectPropertyWithoutDefaultValue = doubleObjectPropertyWithoutDefaultValue;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setMimeTypeCustom(String mimeTypeCustom) {
        this.mimeTypeCustom = mimeTypeCustom;
    }

    public void setStringPropertyWithTooltipInfo(String stringPropertyWithTooltipInfo) {
        this.stringPropertyWithTooltipInfo = stringPropertyWithTooltipInfo;
    }
}

