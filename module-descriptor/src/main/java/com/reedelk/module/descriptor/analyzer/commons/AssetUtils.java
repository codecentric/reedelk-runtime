package com.reedelk.module.descriptor.analyzer.commons;

import io.github.classgraph.Resource;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static java.lang.String.format;

public class AssetUtils {

    private static final String COMPONENT_IMAGE_NAME_TEMPLATE = "%s.png";
    private static final String COMPONENT_ICON_NAME_TEMPLATE = "%s-icon.png";

    private static final Logger LOG = Logger.getLogger(AssetUtils.class);

    private AssetUtils() {
    }

    // Base directory is file:///user/test/classes (from IntelliJ plugin)
    public static Image loadImageFromBaseDirectory(String baseDirectory, String componentFullyQualifiedName) {
        try {
            Path baseDirPath = Paths.get(new URI(baseDirectory));
            Path path = Paths.get(baseDirPath.toString(), imageNameFrom(componentFullyQualifiedName));
            return getImage(path.toUri().toURL());
        } catch (MalformedURLException | URISyntaxException e) {
            // Nothing to do.
            return null;
        }
    }

    // Base directory is file:///user/test/classes (from IntelliJ plugin)
    public static Icon loadIconFromBaseDirectory(String baseDirectory, String componentFullyQualifiedName) {
        try {
            Path baseDirPath = Paths.get(new URI(baseDirectory));
            Path path = Paths.get(baseDirPath.toString(), iconNameFrom(componentFullyQualifiedName));
            return getIcon(path.toUri().toURL());
        } catch (MalformedURLException | URISyntaxException e) {
            // Nothing to do.
            return null;
        }
    }

    public static Image loadImage(String jarFile, String componentFullyQualifiedName) {
        String imageName = imageNameFrom(componentFullyQualifiedName);
        try (JarFile file = new JarFile(jarFile)) {
            ZipEntry componentImage = file.getEntry(imageName);
            if (componentImage != null) {
                try (InputStream inputStream = file.getInputStream(componentImage)) {
                    return ImageIO.read(inputStream);
                }
            }
            return null;
        } catch (FileNotFoundException fileNotFoundException) {
            LOG.info(format("Component Image named=[%s] from jar file=[%s] could not be found. Using default instead.", imageName, jarFile));
            return null;
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
    }

    public static Icon loadIcon(String jarFile, String componentFullyQualifiedName) {
        String iconName = iconNameFrom(componentFullyQualifiedName);
        try (JarFile file = new JarFile(jarFile)) {
            ZipEntry componentIcon = file.getEntry(iconName);
            if (componentIcon != null) {
                try (InputStream inputStream = file.getInputStream(componentIcon)) {
                    return new ImageIcon(ImageIO.read(inputStream));
                }
            }
            return null;
        } catch (FileNotFoundException fileNotFoundException) {
            LOG.info(format("Component Icon named=[%s] from jar file=[%s] could not be found. Using default instead.", iconName, jarFile));
            return null;
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
    }

    public static Image loadImage(ScanResult scanResult, String fullyQualifiedClassName) {
        String imageFileName = imageNameFrom(fullyQualifiedClassName);
        return findResourceMatchingLeafName(scanResult, imageFileName)
                .map(AssetUtils::getImage)
                .orElse(null);
    }

    public static Icon loadIcon(ScanResult scanResult, String fullyQualifiedClassName) {
        String iconFileName = iconNameFrom(fullyQualifiedClassName);
        return findResourceMatchingLeafName(scanResult, iconFileName)
                .map(AssetUtils::getIcon)
                .orElse(null);
    }

    private static Optional<Resource> findResourceMatchingLeafName(ScanResult scanResult, String leafFileName) {
        ResourceList resourcesWithLeafName = scanResult.getResourcesWithLeafName(leafFileName);
        Map<String, ResourceList> items = resourcesWithLeafName.asMap();
        if (items.containsKey(leafFileName)) {
            ResourceList resources = items.get(leafFileName);
            if (!resources.isEmpty()) {
                return Optional.ofNullable(resources.get(0));
            }
        }
        return Optional.empty();
    }

    public static Icon getIcon(URL iconURL) {
        try (InputStream is = iconURL.openStream()) {
            return getIcon(is);
        } catch (FileNotFoundException fileNotFoundException) {
            LOG.info(format("Component Icon from URL=[%s] could not be found. Using default instead.", iconURL.toString()));
            return null;
        } catch (IOException exception) {
            LOG.error(exception);
            return null;
        }
    }

    public static Image getImage(URL imageURL) {
        try (InputStream is = imageURL.openStream()) {
            return getImage(is);
        } catch (FileNotFoundException fileNotFoundException) {
            LOG.info(format("Component Image from URL=[%s] could not be found. Using default instead.", imageURL.toString()));
            return null;
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
    }

    private static Icon getIcon(Resource resource) {
        try (InputStream is = resource.open()) {
            return getIcon(is);
        } catch (IOException exception) {
            LOG.error(exception);
            return null;
        }
    }

    private static Image getImage(Resource resource) {
        try (InputStream is = resource.open()) {
            return getImage(is);
        } catch (IOException exception) {
            LOG.error(exception);
            return null;
        }
    }

    private static Image getImage(InputStream inputStream) throws IOException {
        return ImageIO.read(inputStream);
    }

    private static Icon getIcon(InputStream inputStream) throws IOException {
        return new ImageIcon(ImageIO.read(inputStream));
    }

    private static String imageNameFrom(String componentFullyQualifiedName) {
        return format(COMPONENT_IMAGE_NAME_TEMPLATE, componentFullyQualifiedName);
    }

    private static String iconNameFrom(String componentFullyQualifiedName) {
        return format(COMPONENT_ICON_NAME_TEMPLATE, componentFullyQualifiedName);
    }
}
