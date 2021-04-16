package de.codecentric.reedelk.module.descriptor.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.codecentric.reedelk.module.descriptor.ModuleDescriptorException;
import de.codecentric.reedelk.module.descriptor.model.ModuleDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.Messages.JsonProvider.JSON_DESERIALIZE_ERROR;
import static de.codecentric.reedelk.module.descriptor.analyzer.commons.Messages.JsonProvider.JSON_SERIALIZE_ERROR;

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

    public static String toJson(ModuleDescriptor moduleDescriptor) {
        try {
            return gson.toJson(moduleDescriptor);
        } catch (Exception exception) {
            String error = JSON_SERIALIZE_ERROR.format(exception.getMessage());
            throw new ModuleDescriptorException(error, exception);
        }
    }

    public static ModuleDescriptor fromJson(String json) {
        try {
            return gson.fromJson(json, ModuleDescriptor.class);
        } catch (Exception exception) {
            String error = JSON_DESERIALIZE_ERROR.format(exception.getMessage());
            throw new ModuleDescriptorException(error, exception);
        }
    }

    public static ModuleDescriptor fromURL(URL resource) {
        try (Scanner scanner = new Scanner(resource.openStream(), StandardCharsets.UTF_8.name())) {
            String json = scanner.useDelimiter("\\A").next();
            return JsonProvider.fromJson(json);
        } catch (IOException exception) {
            String error = JSON_DESERIALIZE_ERROR.format(exception.getMessage());
            LOG.warn(error, exception);
            return new ModuleDescriptor();
        }
    }
}
