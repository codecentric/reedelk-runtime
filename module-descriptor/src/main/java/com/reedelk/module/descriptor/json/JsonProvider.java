package com.reedelk.module.descriptor.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reedelk.module.descriptor.ModuleDescriptor;
import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.model.TypeDescriptor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class JsonProvider {

    private static final Logger LOG = Logger.getLogger(JsonProvider.class);

    private static final String JSON_CHARSET = "UTF-8";
    private static final Gson gson;
    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(TypeDescriptor.class, new TypeDescriptorSerializer());
        gsonBuilder.registerTypeAdapter(TypeDescriptor.class, new TypeDescriptorDeserializer());
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
        try (Scanner scanner = new Scanner(resource.openStream(), JSON_CHARSET)) {
            String json = scanner.useDelimiter("\\A").next();
            return JsonProvider.fromJson(json);
        } catch (IOException e) {
            LOG.warn(e);
            return new ModuleDescriptor();
        }
    }
}
