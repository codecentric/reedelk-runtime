package com.reedelk.module.descriptor.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.model.ModuleDescriptor;
import com.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class JsonProvider {

    private static final Logger LOG = Logger.getLogger(JsonProvider.class);

    private static final Gson gson;
    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(PropertyTypeDescriptor.class, new PropertyTypeDescriptorSerializer());
        gsonBuilder.registerTypeAdapter(PropertyTypeDescriptor.class, new PropertyTypeDescriptorDeserializer());
        gson = gsonBuilder.create();
    }

    private JsonProvider() {
    }

    public static String toJson(ModuleDescriptor moduleDescriptor) throws ModuleDescriptorException {
        try {
            return gson.toJson(moduleDescriptor);
        } catch (Exception e) {
            throw new ModuleDescriptorException("Could not write JSON From Module Descriptor", e);
        }
    }

    public static ModuleDescriptor fromJson(String json) {
        return gson.fromJson(json, ModuleDescriptor.class);
    }

    public static ModuleDescriptor fromURL(URL resource) {
        try (Scanner scanner = new Scanner(resource.openStream(), StandardCharsets.UTF_8.name())) {
            String json = scanner.useDelimiter("\\A").next();
            return JsonProvider.fromJson(json);
        } catch (IOException e) {
            LOG.warn(e);
            return new ModuleDescriptor();
        }
    }
}
