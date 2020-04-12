package com.reedelk.esb.services.module;

import com.reedelk.runtime.api.exception.PlatformException;
import com.reedelk.runtime.system.api.ModuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncModuleServiceTest {

    @Mock
    private ModuleService moduleService;
    @Mock
    private Bundle bundle1;
    @Mock
    private Bundle bundle2;
    @Mock
    private BundleContext context;

    private SyncModuleService service;

    @BeforeEach
    void setUp() {
        service = spy(new SyncModuleService(moduleService, context));
        lenient()
                .doReturn(new Bundle[] {bundle1, bundle2})
                .when(context)
                .getBundles();
    }

    @Test
    void shouldUnInstallIfModuleExistsAlready() {
        // Given
        String myModuleName1 = "myModuleName1";
        String myModuleName2 = "myModuleName2";
        String myModuleName2JarPath = "/Users/user/my/module2";

        doReturn(myModuleName1).when(bundle1).getSymbolicName();
        doReturn(myModuleName2).when(bundle2).getSymbolicName();
        doReturn(myModuleName2JarPath).when(bundle2).getLocation();

        doReturn(Optional.of(myModuleName2))
                .when(service)
                .moduleNameOf(myModuleName2JarPath);

        // When
        service.unInstallIfModuleExistsAlready(myModuleName2JarPath);

        // Then
        verify(moduleService).uninstall(myModuleName2JarPath);
    }

    @Test
    void shouldNotUnInstallIfModuleDoesNotExists() {
        // Given
        String myModuleName1 = "myModuleName1";
        String myModuleName2 = "myModuleName2";

        String myNotExistentModuleName = "myNotExistentModuleName";
        String notExistentModule = "/Users/user/my/not/existent/module";

        doReturn(myModuleName1).when(bundle1).getSymbolicName();
        doReturn(myModuleName2).when(bundle2).getSymbolicName();

        doReturn(Optional.of(myNotExistentModuleName))
                .when(service)
                .moduleNameOf(notExistentModule);

        // When
        service.unInstallIfModuleExistsAlready(notExistentModule);

        // Then
        verify(moduleService, never()).uninstall(notExistentModule);
    }

    @Test
    void shouldThrowExceptionWhenModuleNameCouldNotBeFound() {
        // Given
        String myModuleJarPath = "/Users/user/my/not/existent/module";

        doReturn(Optional.empty()).when(service).moduleNameOf(myModuleJarPath);

        // When
        PlatformException thrown = assertThrows(PlatformException.class, () ->
                service.unInstallIfModuleExistsAlready(myModuleJarPath));

        // Then
        verifyNoMoreInteractions(moduleService);
        assertThat(thrown).hasMessage("Install failed: could not find module name from file path=[/Users/user/my/not/existent/module]");
    }
}
