package de.codecentric.reedelk.runtime.validator;

import java.io.File;

import static de.codecentric.reedelk.runtime.commons.RuntimeMessage.message;

public class DirectoryExistsValidator implements Validator {

    private final String directoryPathToCheck;

    public DirectoryExistsValidator(String directoryPathToCheck) {
        this.directoryPathToCheck = directoryPathToCheck;
    }

    @Override
    public boolean validate() {
        File directoryToVerify = new File(directoryPathToCheck);
        return directoryToVerify.exists() && directoryToVerify.isDirectory();
    }

    @Override
    public String error() {
        return message("directory.could.not.be.found", directoryPathToCheck);
    }

}
