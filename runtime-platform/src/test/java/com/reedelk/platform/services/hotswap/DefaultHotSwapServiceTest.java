package com.reedelk.platform.services.hotswap;

import com.reedelk.runtime.system.api.ModuleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultHotSwapServiceTest {

    @Mock
    private Bundle bundle;
    @Mock
    private BundleContext context;
    @Mock
    private HotSwapListener listener;

    private DefaultHotSwapService service;

    @BeforeEach
    void setUp() {
        service = new DefaultHotSwapService(context, listener);
    }

    @Test
    void shouldReturnIdOfTheHotSwappedModule() throws ModuleNotFoundException {
        // Given
        String modulePath = "file:/Users/myuser/module/path/test-module-1.0.0.jar";
        String resourcesRootDirectory = "/Users/myuser/module/src/main/resources";

        doReturn(bundle).when(context).getBundle(modulePath);
        doReturn(887L).when(bundle).getBundleId();

        // When
        long hotSwappedModuleId = service.hotSwap(modulePath, resourcesRootDirectory);

        // Then
        verify(listener).hotSwap(887L, resourcesRootDirectory);
        verifyNoMoreInteractions(listener);

        assertThat(hotSwappedModuleId).isEqualTo(887L);
    }
}
