package com.reedelk.runtime.license;

import com.reedelk.runtime.Application;
import com.reedelk.runtime.ESBRuntimeException;
import com.reedelk.runtime.adminconsole.AdminConsoleInstallTask;
import com.reedelk.runtime.commons.ApplicationTask;
import com.reedelk.runtime.commons.FileExtension;
import com.reedelk.runtime.commons.FileUtils;
import com.reedelk.runtime.properties.SystemConfiguration;

import java.io.File;

import static com.reedelk.runtime.commons.RuntimeMessage.message;

public class LicenseVerifierTask extends ApplicationTask {

    public static void execute(Application application) {
        new Thread(new LicenseVerifierTask(application)).start();
    }

    private LicenseVerifierTask(final Application application) {
        super(application);
    }

    @Override
    protected void doRun() {
        String configDirectory = SystemConfiguration.configDirectory();
        File licenseFile =
                FileUtils.findFilesFromDirectoryWithExt(configDirectory, FileExtension.LICENSE)
                        .orElseThrow(() -> new ESBRuntimeException(message("runtime.license.not.found")));

        LicenseVerifier licenseVerifier = new LicenseVerifier(licenseFile);
        try {
            licenseVerifier.read();
        } catch (Exception exception) {
            throw new ESBRuntimeException(message("runtime.license.not.valid"));
        }

        boolean isValid = licenseVerifier.isValid(); // valid meaning if it has been signed correctly.
        if (!isValid) {
            throw new ESBRuntimeException(message("runtime.license.not.valid"));
        }

        boolean isExpired = licenseVerifier.isExpired(); // the license might be expired
        if (isExpired) {
            throw new ESBRuntimeException(message("runtime.license.expired"));
        }
    }
}
