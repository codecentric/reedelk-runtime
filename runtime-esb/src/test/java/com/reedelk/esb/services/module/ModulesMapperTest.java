package com.reedelk.esb.services.module;

import com.reedelk.esb.flow.Flow;
import com.reedelk.esb.module.Module;
import com.reedelk.esb.module.ModuleDeserializer;
import com.reedelk.esb.module.state.ModuleState;
import com.reedelk.runtime.system.api.FlowDto;
import com.reedelk.runtime.system.api.ModuleDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ModulesMapperTest {

    @Mock
    private ModuleDeserializer deserializer;

    private final long TEST_MODULE_ID = 12;
    private final String TEST_MODULE_NAME = "ModuleNameTest";
    private final String TEST_VERSION = "0.9.0";
    private final String TEST_LOCATION = "file://location/test";

    private ModulesMapper mapper = new ModulesMapper();
    private Module module;

    @Test
    void shouldCorrectlyMapModuleToDtoWhenStateIsInstalled() {
        // Given
        module = Module.builder()
                .version(TEST_VERSION)
                .name(TEST_MODULE_NAME)
                .moduleId(TEST_MODULE_ID)
                .deserializer(deserializer)
                .moduleFilePath(TEST_LOCATION)
                .build();

        // When
        ModuleDto dto = mapper.map(module);

        // Then
        assertThat(dto.getName()).isEqualTo(TEST_MODULE_NAME);
        assertThat(dto.getModuleId()).isEqualTo(TEST_MODULE_ID);
        assertThat(dto.getModuleFilePath()).isEqualTo(TEST_LOCATION);
        assertThat(dto.getState()).isEqualTo(ModuleState.INSTALLED.name());
        assertThat(dto.getVersion()).isEqualTo(TEST_VERSION);
    }

    @Test
    void shouldCorrectlyMapModuleToDtoWenStateIsStarted() {
        // Given
        Flow flow1 = mockFlow("aabbcc", "Flow 1 title");
        Flow flow2 = mockFlow("ddeeff", "Flow 2 title");

        module = Module.builder()
                .version(TEST_VERSION)
                .name(TEST_MODULE_NAME)
                .moduleId(TEST_MODULE_ID)
                .deserializer(deserializer)
                .moduleFilePath(TEST_LOCATION)
                .build();
        module.unresolve(Collections.emptyList(), Collections.emptyList());
        module.resolve(Collections.emptyList());
        module.stop(Arrays.asList(flow1, flow2));
        module.start(Arrays.asList(flow1, flow2));

        // When
        ModuleDto dto = mapper.map(module);

        // Then
        assertThat(dto.getName()).isEqualTo(TEST_MODULE_NAME);
        assertThat(dto.getModuleId()).isEqualTo(TEST_MODULE_ID);
        assertThat(dto.getModuleFilePath()).isEqualTo(TEST_LOCATION);
        assertThat(dto.getState()).isEqualTo(ModuleState.STARTED.name());
        assertThat(dto.getVersion()).isEqualTo(TEST_VERSION);


        Collection<FlowDto> flowsDTOs = dto.getFlows();
        assertThatExistsFlowWith(flowsDTOs, "aabbcc", "Flow 1 title");
        assertThatExistsFlowWith(flowsDTOs, "ddeeff", "Flow 2 title");

    }

    private void assertThatExistsFlowWith(Collection<FlowDto> flowsDTOs, String flowId, String flowTitle) {
        boolean exists = flowsDTOs.stream()
                .anyMatch(flowDto -> flowId.equals(flowDto.getId()) && flowTitle.equals(flowDto.getTitle()));
        assertThat(exists).isTrue();
    }

    private Flow mockFlow(String id, String title) {
        Flow flow = mock(Flow.class);
        doReturn(id).when(flow).getFlowId();
        doReturn(Optional.of(title)).when(flow).getFlowTitle();
        return flow;
    }
}