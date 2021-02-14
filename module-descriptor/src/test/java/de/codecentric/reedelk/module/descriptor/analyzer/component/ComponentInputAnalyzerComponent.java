package de.codecentric.reedelk.module.descriptor.analyzer.component;

import de.codecentric.reedelk.runtime.api.annotation.ComponentInput;

@ComponentInput(payload = {String.class, byte[].class}, description = "My input description")
public class ComponentInputAnalyzerComponent {
}
