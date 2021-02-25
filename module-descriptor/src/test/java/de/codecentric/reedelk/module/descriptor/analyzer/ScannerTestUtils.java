package de.codecentric.reedelk.module.descriptor.analyzer;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class ScannerTestUtils {

    public static ScanResult scanWithResult(Class<?> targetComponentClazz) {
        return new ClassGraph()
                .whitelistPackages(targetComponentClazz.getPackage().getName())
                .enableFieldInfo()
                .enableMethodInfo()
                .enableAnnotationInfo()
                .ignoreFieldVisibility()
                .scan();
    }

    public static ScanContext scan(Class<?> targetComponentClazz) {
        ScanResult scanResult = scanWithResult(targetComponentClazz);
        ComponentAnalyzerContext context = new ComponentAnalyzerContext(scanResult);
        ClassInfo targetComponentClassInfo = scanResult.getClassInfo(targetComponentClazz.getName());
        return new ScanContext(context, targetComponentClassInfo);
    }

    public static class ScanContext {

        public final ComponentAnalyzerContext context;
        public final ClassInfo targetComponentClassInfo;

        ScanContext(ComponentAnalyzerContext context, ClassInfo targetComponentClassInfo) {
            this.context = context;
            this.targetComponentClassInfo = targetComponentClassInfo;
        }
    }
}
