package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.commons.Messages;
import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.ComponentPropertyDescriptor;
import com.reedelk.module.descriptor.model.WhenDescriptor;
import com.reedelk.runtime.api.annotation.When;
import com.reedelk.runtime.api.annotation.Whens;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValueList;
import io.github.classgraph.FieldInfo;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhenFieldInfoAnalyzer implements FieldInfoAnalyzer {

    private static final Logger LOG = Logger.getLogger(WhenFieldInfoAnalyzer.class);

    @Override
    public void handle(FieldInfo propertyInfo, ComponentPropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        // Check if there is a single 'when' annotation
        Collection<AnnotationInfo> whensAnnotations = findWhensAnnotations(propertyInfo);
        for (AnnotationInfo info : whensAnnotations) {
            WhenDescriptor variableDefinition = processWhenInfo(info);
            builder.when(variableDefinition);
        }

        // More than one 'when' definition
        // Important: be careful here. The Whens is only applied when the class is compiled because
        // the compiler replaces 2 @When with 1 @Whens({@When1,@When2}). This code must not be removed.
        boolean hasWhenAnnotations = ScannerUtils.hasAnnotation(propertyInfo, Whens.class);
        if (hasWhenAnnotations) {
            AnnotationInfo annotationInfo = propertyInfo.getAnnotationInfo(Whens.class.getName());
            AnnotationParameterValueList whensAnnotationList = annotationInfo.getParameterValues();
            Object[] annotationInfos = (Object[]) whensAnnotationList.get(0).getValue();
            for (Object info : annotationInfos) {
                try {
                    WhenDescriptor whenDescriptor = processWhenInfo((AnnotationInfo) info);
                    builder.when(whenDescriptor);
                } catch (Exception exception) {
                    String message  = String.format(Messages.ERROR_WHEN_ANNOTATION, propertyInfo.getName());
                    LOG.warn(message, exception);
                }
            }
        }
    }

    private WhenDescriptor processWhenInfo(AnnotationInfo info) {
        String propertyName = ScannerUtils.stringParameterValueFrom(info, "propertyName");
        String propertyValue = ScannerUtils.stringParameterValueFrom(info, "propertyValue");
        WhenDescriptor definition = new WhenDescriptor();
        definition.setPropertyName(propertyName);
        definition.setPropertyValue(propertyValue);
        return definition;
    }

    private Collection<AnnotationInfo> findWhensAnnotations(FieldInfo propertyInfo) {
        List<AnnotationInfo> whenAnnotationInfos = new ArrayList<>();
        for (AnnotationInfo info : propertyInfo.getAnnotationInfo()) {
            if (info.getName().equals(When.class.getName())) {
                whenAnnotationInfos.add(info);
            }
        }
        return whenAnnotationInfos;
    }
}