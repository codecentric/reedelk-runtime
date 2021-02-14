package de.codecentric.reedelk.module.descriptor.analyzer.commons;

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

    public static Icon getIcon(URL iconURL) {
        if (iconURL == null) return null;
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
        if (imageURL == null) return null;
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
