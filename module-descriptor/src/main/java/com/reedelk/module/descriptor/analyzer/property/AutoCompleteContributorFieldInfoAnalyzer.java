package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.AutoCompleteContributorDescriptor;
import com.reedelk.module.descriptor.model.ComponentPropertyDescriptor;
import com.reedelk.runtime.api.annotation.AutoCompleteContributor;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.FieldInfo;

import java.util.List;

public class AutoCompleteContributorFieldInfoAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo propertyInfo, ComponentPropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        if (ScannerUtils.hasAnnotation(propertyInfo, AutoCompleteContributor.class)) {
            AnnotationInfo info = propertyInfo.getAnnotationInfo(AutoCompleteContributor.class.getName());

            boolean isMessage =  ScannerUtils.booleanParameterValueFrom(info, "message", true);
            boolean isError =  ScannerUtils.booleanParameterValueFrom(info, "error", false);
            boolean isContext = ScannerUtils.booleanParameterValueFrom(info, "context", true);
            List<String> customContributions = ScannerUtils.stringListParameterValueFrom(info, "contributions");

            AutoCompleteContributorDescriptor definition = new AutoCompleteContributorDescriptor();
            definition.setMessage(isMessage);
            definition.setError(isError);
            definition.setContext(isContext);
            definition.setContributions(customContributions);
            builder.autoCompleteContributor(definition);
        }
    }
}
