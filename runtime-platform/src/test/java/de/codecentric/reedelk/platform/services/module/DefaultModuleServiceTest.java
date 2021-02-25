package de.codecentric.reedelk.platform.services.module;

import de.codecentric.reedelk.platform.module.ModulesManager;
import de.codecentric.reedelk.runtime.system.api.SystemProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultModuleServiceTest {

    @Mock
    private BundleContext mockContext;
    @Mock
    private EventListener mockEventListener;
    @Mock
    private ModulesManager mockModulesManager;
    @Mock
    private SystemProperty mockSystemProperty;
    @Mock
    private Bundle mockBundle;

    private DefaultModuleService service;

    @BeforeEach
    void setUp() {
        service = spy(new DefaultModuleService(mockContext, mockModulesManager, mockSystemProperty, mockEventListener));
    }

    @Test
    void shouldCorrectlyInstallAndStartModuleFromJarPath() throws BundleException {
        // Given
        long expectedModuleId = 10L;
        String moduleJarPath = "/Users/local/project/modules/module-test.jar";
        doReturn(null).when(mockContext).getBundle(moduleJarPath); // not installed yet.
        doReturn(mockBundle).when(mockContext).installBundle(moduleJarPath);
        doReturn(expectedModuleId).when(mockBundle).getBundleId();
        doNothing().when(service).unInstallIfModuleExistsAlready(moduleJarPath);
        doNothing().when(service).checkIsValidModuleOrUnInstallAndThrow(moduleJarPath);

        // When
        long installedModuleId = service.install(moduleJarPath);

        // Then
        assertThat(installedModuleId).isEqualTo(expectedModuleId);
        verify(mockBundle).start();
        verify(service).unInstallIfModuleExistsAlready(moduleJarPath);
    }

    @Test
    void shouldCorrectlyUpdateStartedModuleAtJarPath() throws BundleException {
        // Given
        long expectedModuleId = 10L;
        String moduleJarPath = "/Users/local/project/modules/module-test.jar";
        doReturn(mockBundle).when(mockContext).getBundle(moduleJarPath);
        doReturn(Bundle.ACTIVE).when(mockBundle).getState();
        doReturn(expectedModuleId).when(mockBundle).getBundleId();

        InOrder inOrder = inOrder(mockBundle);
        // When
        long updatedModuleId = service.update(moduleJarPath);

        // Then
        inOrder.verify(mockBundle).stop();
        inOrder.verify(mockBundle).update();
        inOrder.verify(mockBundle).start();
        assertThat(updatedModuleId).isEqualTo(expectedModuleId);
        verify(mockEventListener).moduleStopping(expectedModuleId);
    }

    @Test
    void shouldCorrectlyUpdateInstalledModuleAtJarPath() throws BundleException {
        // Given
        long expectedModuleId = 10L;
        String moduleJarPath = "/Users/local/project/modules/module-test.jar";
        doReturn(mockBundle).when(mockContext).getBundle(moduleJarPath);
        doReturn(Bundle.INSTALLED).when(mockBundle).getState();
        doReturn(expectedModuleId).when(mockBundle).getBundleId();

        InOrder inOrder = inOrder(mockBundle);
        // When
        long updatedModuleId = service.update(moduleJarPath);

        // Then
        inOrder.verify(mockBundle).update();
        inOrder.verify(mockBundle).start();
        assertThat(updatedModuleId).isEqualTo(expectedModuleId);
        verify(mockEventListener, never()).moduleStopping(expectedModuleId);
    }

    @Test
    void shouldCorrectlyUninstallModuleAtJarPath() throws BundleException {
        // Given
        long expectedModuleId = 10L;
        String moduleJarPath = "/Users/local/project/modules/module-test.jar";
        doReturn(mockBundle).when(mockContext).getBundle(moduleJarPath);
        doReturn(expectedModuleId).when(mockBundle).getBundleId();
        doReturn((DefaultModuleService.Operation) bundle -> {
            // no-op
        }).when(service).deleteModuleBundleJarOperation();

        InOrder inOrder = inOrder(mockBundle);

        // When
        long unInstalledModuleId = service.uninstall(moduleJarPath);

        // Then
        inOrder.verify(mockBundle).stop();
        inOrder.verify(mockBundle).uninstall();
        assertThat(unInstalledModuleId).isEqualTo(expectedModuleId);
        verify(mockEventListener).moduleStopping(expectedModuleId);
        verify(service).deleteModuleBundleJarOperation();
    }
}
