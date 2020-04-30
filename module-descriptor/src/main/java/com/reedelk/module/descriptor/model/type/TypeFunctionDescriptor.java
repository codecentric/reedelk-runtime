package com.reedelk.module.descriptor.model.type;

public class TypeFunctionDescriptor {

    private String name;
    private String example;
    private String signature;
    private String returnType;
    private String description;

    private int cursorOffset;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCursorOffset() {
        return cursorOffset;
    }

    public void setCursorOffset(int cursorOffset) {
        this.cursorOffset = cursorOffset;
    }
}
