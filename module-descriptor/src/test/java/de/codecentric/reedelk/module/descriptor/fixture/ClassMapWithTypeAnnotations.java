package de.codecentric.reedelk.module.descriptor.fixture;

import de.codecentric.reedelk.runtime.api.annotation.Type;
import de.codecentric.reedelk.runtime.api.annotation.TypeProperty;

import java.io.Serializable;
import java.util.HashMap;

@Type
@TypeProperty(name = ClassMapWithTypeAnnotations.WEB_CONTENT_LINK, type = String.class)
@TypeProperty(name = ClassMapWithTypeAnnotations.WEB_VIEW_LINK, type = String.class)
@TypeProperty(name = ClassMapWithTypeAnnotations.NAME, type = String.class)
@TypeProperty(name = ClassMapWithTypeAnnotations.ID, type = String.class)
public class ClassMapWithTypeAnnotations extends HashMap<String, Serializable> {

    static final String WEB_CONTENT_LINK = "webContentLink";
    static final String WEB_VIEW_LINK = "webViewLink";
    static final String NAME = "name";
    static final String ID = "id";
}
