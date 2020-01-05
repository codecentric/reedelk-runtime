package com.reedelk.runtime.rest.api;

import com.reedelk.runtime.rest.api.health.v1.HealthGETRes;
import com.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTReq;
import com.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTRes;
import com.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTResNotFound;
import com.reedelk.runtime.rest.api.module.v1.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.reedelk.runtime.rest.api.InternalAPI.Health;
import static com.reedelk.runtime.rest.api.InternalAPI.HotSwap;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class InternalAPITest {

    @Nested
    @DisplayName("Health API")
    class HealthAPI {

        @Test
        void shouldHealthGETResSerializeCorrectly() {
            // Given
            HealthGETRes dto = new HealthGETRes();
            dto.setStatus("UP");
            dto.setVersion("2.1.3-SNAPSHOT");

            // When
            String actual = Health.V1.GET.Res.serialize(dto);

            // Then
            String expected = JSONS.HealthGETRes.string();
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
        }

        @Test
        void shouldHealthGETResDeserializeCorrectly() {
            // Given
            String json = JSONS.HealthGETRes.string();

            // When
            HealthGETRes actual = Health.V1.GET.Res.deserialize(json);

            // Then
            assertThat(actual).isNotNull();
            assertThat(actual.getStatus()).isEqualTo("UP");
            assertThat(actual.getVersion()).isEqualTo("2.1.3-SNAPSHOT");
        }
    }

    @Nested
    @DisplayName("Module API")
    class ModuleAPI {

        @Test
        void shouldModulesGETResSerializeCorrectly() {
            // Given
            FlowGETRes flow1 = new FlowGETRes();
            flow1.setId("aabbccdd");
            flow1.setTitle("Flow1 title");

            FlowGETRes flow2 = new FlowGETRes();
            flow2.setId("eeffgghh");
            flow2.setTitle("Flow2 title");

            ModuleGETRes module1 = new ModuleGETRes();
            module1.setName("HttpComponents");
            module1.setState("INSTALLED");
            module1.setModuleId(234L);
            module1.setVersion("0.9.0");
            module1.setModuleFilePath("file:/Users/test/testingflows/target/HttpComponents-0.9.0.jar");
            module1.setFlows(asList(flow1, flow2));
            module1.setErrors(asList("exception1", "exception2"));
            module1.setResolvedComponents(asList("com.reedelk.runtime.component.1", "com.reedelk.runtime.component.2"));
            module1.setUnresolvedComponents(asList("com.reedelk.runtime.component.3", "com.reedelk.runtime.component.4"));

            ModuleGETRes module2 = new ModuleGETRes();
            module2.setName("MyFlows");
            module2.setState("STARTED");
            module2.setModuleId(12L);
            module2.setVersion("0.9.0");
            module2.setModuleFilePath("file:/Users/test/testingflows/target/myflows-0.9.0.jar");
            module2.setErrors(asList("exception3", "exception4"));
            module2.setResolvedComponents(asList("com.reedelk.runtime.component.X1", "com.reedelk.runtime.component.X2"));
            module2.setUnresolvedComponents(asList("com.reedelk.runtime.component.X4", "com.reedelk.runtime.component.X5"));

            Set<ModuleGETRes> modules = new HashSet<>();
            modules.add(module1);
            modules.add(module2);

            ModulesGETRes dto = new ModulesGETRes();
            dto.setModules(modules);

            // When
            String actual = InternalAPI.Module.V1.GET.Res.serialize(dto);

            // Then
            String expected = JSONS.ModulesGETRes.string();
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
        }

        @Test
        void shouldModulesGETResDeserializeCorrectly() {
            // Given
            String json = JSONS.ModulesGETRes.string();

            // When
            ModulesGETRes actual = InternalAPI.Module.V1.GET.Res.deserialize(json);

            // Then
            assertThat(actual).isNotNull();

            Collection<ModuleGETRes> modules = actual.getModules();
            assertThat(modules).hasSize(2);


            ModuleGETRes module1 = new ModuleGETRes();
            module1.setName("HttpComponents");
            module1.setState("INSTALLED");
            module1.setVersion("0.9.0");
            module1.setModuleId(234L);
            module1.setModuleFilePath("file:/Users/test/testingflows/target/HttpComponents-0.9.0.jar");
            module1.setErrors(asList("exception1", "exception2"));
            module1.setResolvedComponents(asList("com.reedelk.runtime.component.1", "com.reedelk.runtime.component.2"));
            module1.setUnresolvedComponents(asList("com.reedelk.runtime.component.3", "com.reedelk.runtime.component.4"));

            assertExistsModule(modules, module1);


            ModuleGETRes module2 = new ModuleGETRes();
            module2.setName("MyFlows");
            module2.setState("STARTED");
            module2.setVersion("0.9.0");
            module2.setModuleId(12L);
            module2.setModuleFilePath("file:/Users/test/testingflows/target/myflows-0.9.0.jar");
            module2.setErrors(asList("exception3", "exception4"));
            module2.setResolvedComponents(asList("com.reedelk.runtime.component.X1", "com.reedelk.runtime.component.X2"));
            module2.setUnresolvedComponents(asList("com.reedelk.runtime.component.X4", "com.reedelk.runtime.component.X5"));

            assertExistsModule(modules, module2);
        }

        @Test
        void shouldModulePUTReqSerializeCorrectly() {
            // Given
            ModulePUTReq dto = new ModulePUTReq();
            dto.setModuleFilePath("file://my/file/path");

            // When
            String actual = InternalAPI.Module.V1.PUT.Req.serialize(dto);

            // Then
            String expected = JSONS.ModulePUTReq.string();
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
        }

        @Test
        void shouldModulePUTReqDeserializeCorrectly() {
            // Given
            String json = JSONS.ModulePUTReq.string();

            // When
            ModulePUTReq actual = InternalAPI.Module.V1.PUT.Req.deserialize(json);

            // Then
            assertThat(actual).isNotNull();
            assertThat(actual.getModuleFilePath()).isEqualTo("file://my/file/path");
        }

        @Test
        void shouldModulePUTResSerializeCorrectly() {
            // Given
            ModulePUTRes dto = new ModulePUTRes();
            dto.setModuleId(665L);

            // When
            String actual = InternalAPI.Module.V1.PUT.Res.serialize(dto);

            // Then
            String expected = JSONS.ModulePUTRes.string();
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
        }

        @Test
        void shouldModulePUTResDeserializeCorrectly() {
            // Given
            String json = JSONS.ModulePUTRes.string();

            ModulePUTRes actual = InternalAPI.Module.V1.PUT.Res.deserialize(json);

            // Then
            assertThat(actual).isNotNull();
            assertThat(actual.getModuleId()).isEqualTo(665L);
        }

        @Test
        void shouldModulePOSTReqSerializeCorrectly() {
            // Given
            ModulePOSTReq dto = new ModulePOSTReq();
            dto.setModuleFilePath("file://my/file/path");

            // When
            String actual = InternalAPI.Module.V1.POST.Req.serialize(dto);

            // Then
            String expected = JSONS.ModulePOSTReq.string();
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
        }

        @Test
        void shouldModulePOSTReqDeserializeCorrectly() {
            // Given
            String json = JSONS.ModulePOSTReq.string();

            // When
            ModulePOSTReq actual = InternalAPI.Module.V1.POST.Req.deserialize(json);

            // Then
            assertThat(actual).isNotNull();
            assertThat(actual.getModuleFilePath()).isEqualTo("file://my/file/path");
        }

        @Test
        void shouldModulePOSTResSerializeCorrectly() {
            // Given
            ModulePOSTRes dto = new ModulePOSTRes();
            dto.setModuleId(485L);

            // When
            String actual = InternalAPI.Module.V1.POST.Res.serialize(dto);

            // Then
            String expected = JSONS.ModulePOSTRes.string();
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
        }

        @Test
        void shouldModulePOSTResDeserializeCorrectly() {
            // Given
            String json = JSONS.ModulePOSTRes.string();

            ModulePOSTRes actual = InternalAPI.Module.V1.POST.Res.deserialize(json);

            // Then
            assertThat(actual).isNotNull();
            assertThat(actual.getModuleId()).isEqualTo(485L);
        }

        @Test
        void shouldModuleDELETEReqSerializeCorrectly() {
            // Given
            ModuleDELETEReq dto = new ModuleDELETEReq();
            dto.setModuleFilePath("file://my/file/path");

            // When
            String actual = InternalAPI.Module.V1.DELETE.Req.serialize(dto);

            // Then
            String expected = JSONS.ModuleDELETEReq.string();
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
        }

        @Test
        void shouldModuleDELETEReqDeserializeCorrectly() {
            // Given
            String json = JSONS.ModuleDELETEReq.string();

            // When
            ModuleDELETEReq actual = InternalAPI.Module.V1.DELETE.Req.deserialize(json);

            // Then
            assertThat(actual).isNotNull();
            assertThat(actual.getModuleFilePath()).isEqualTo("file://my/file/path");
        }

        @Test
        void shouldModuleDELETEResSerializeCorrectly() {
            // Given
            ModuleDELETERes dto = new ModuleDELETERes();
            dto.setModuleId(23L);

            // When
            String actual = InternalAPI.Module.V1.DELETE.Res.serialize(dto);

            // Then
            String expected = JSONS.ModuleDELETERes.string();
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
        }

        @Test
        void shouldModuleDELETEResDeserializeCorrectly() {
            // Given
            String json = JSONS.ModuleDELETERes.string();

            ModuleDELETERes actual = InternalAPI.Module.V1.DELETE.Res.deserialize(json);

            // Then
            assertThat(actual).isNotNull();
            assertThat(actual.getModuleId()).isEqualTo(23L);
        }
    }


    @Nested
    @DisplayName("HotSwap API")
    class HotSwapAPI {


        @Test
        void shouldHotSwapPOSTReqSerializeCorrectly() {
            // Given
            HotSwapPOSTReq dto = new HotSwapPOSTReq();
            dto.setModuleFilePath("/my/file/path/my-module.jar");
            dto.setResourcesRootDirectory("/users/john/projects/src/main/resources");

            // When
            String actual = HotSwap.V1.POST.Req.serialize(dto);

            // Then
            String expected = JSONS.HotSwapPOSTReq.string();
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
        }

        @Test
        void shouldHotSwapPOSTReqDeserializeCorrectly() {
            // Given
            String json = JSONS.HotSwapPOSTReq.string();

            // When
            HotSwapPOSTReq actual = HotSwap.V1.POST.Req.deserialize(json);

            // Then
            assertThat(actual).isNotNull();
            assertThat(actual.getModuleFilePath()).isEqualTo("/my/file/path/my-module.jar");
            assertThat(actual.getResourcesRootDirectory()).isEqualTo("/users/john/projects/src/main/resources");
        }

        @Test
        void shouldHotSwapPOSTResSerializeCorrectly() {
            // Given
            HotSwapPOSTRes dto = new HotSwapPOSTRes();
            dto.setModuleId(10L);

            // When
            String actual = HotSwap.V1.POST.Res.serialize(dto);

            // Then
            String expected = JSONS.HotSwapPOSTRes.string();
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
        }

        @Test
        void shouldHotSwapPOSTResDeserializeCorrectly() {
            // Given
            String json = JSONS.HotSwapPOSTRes.string();

            // When
            HotSwapPOSTRes actual = HotSwap.V1.POST.Res.deserialize(json);

            // Then
            assertThat(actual).isNotNull();
            assertThat(actual.getModuleId()).isEqualTo(10L);
        }

        @Test
        void shouldHotSwapPOSTResNotFoundSerializeCorrectly() {
            // Given
            HotSwapPOSTResNotFound dto = new HotSwapPOSTResNotFound();
            dto.setModuleFilePath("/my/not/found/file/path/my-module.jar");
            dto.setResourcesRootDirectory("/users/john/projects/src/main/resources/not/found");

            // When
            String actual = HotSwap.V1.POST.ResNotFound.serialize(dto);

            // Then
            String expected = JSONS.HotSwapPOSTResNotFound.string();
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
        }

        @Test
        void shouldHotSwapPOSTResNotFoundDeserializeCorrectly() {
            // Given
            String json = JSONS.HotSwapPOSTResNotFound.string();

            // When
            HotSwapPOSTResNotFound actual = HotSwap.V1.POST.ResNotFound.deserialize(json);

            // Then
            assertThat(actual).isNotNull();
            assertThat(actual.getModuleFilePath()).isEqualTo("/my/not/found/file/path/my-module.jar");
            assertThat(actual.getResourcesRootDirectory()).isEqualTo("/users/john/projects/src/main/resources/not/found");
        }

    }
    private void assertExistsModule(Collection<ModuleGETRes> modules, ModuleGETRes expected) {
        for (ModuleGETRes module : modules) {
            String name = module.getName();
            if (expected.getName().equals(name)) {
                assertThatContainsInAnyOrder(module.getErrors(), expected.getErrors());
                assertThatContainsInAnyOrder(module.getUnresolvedComponents(), expected.getUnresolvedComponents());
                assertThatContainsInAnyOrder(module.getResolvedComponents(), expected.getResolvedComponents());
                assertThat(module.getState()).isEqualTo(expected.getState());
                return;
            }
        }
        Assertions.fail("Could not find matching module");
    }

    private void assertThatContainsInAnyOrder(Collection<String> actual, Collection<String> expected) {
        String[] array = expected.toArray(new String[]{});
        assertThat(actual).containsExactlyInAnyOrder(array);
    }
}