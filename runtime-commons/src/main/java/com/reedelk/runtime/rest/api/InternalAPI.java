package com.reedelk.runtime.rest.api;

import com.reedelk.runtime.rest.api.health.v1.HealthGETRes;
import com.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTReq;
import com.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTRes;
import com.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTResNotFound;
import com.reedelk.runtime.rest.api.module.v1.*;
import com.reedelk.runtime.rest.api.object.mapper.JSONDeserializer;
import com.reedelk.runtime.rest.api.object.mapper.JSONSerializer;

public class InternalAPI {

    public static class Health {
        public static class V1 {

            public static class GET {

                public static class Res {
                    public static HealthGETRes deserialize(String json) {
                        return JSONDeserializer.deserialize(json, HealthGETRes.class);
                    }
                    public static String serialize(HealthGETRes input) {
                        return JSONSerializer.serialize(input);
                    }
                }
            }
        }
    }

    public static class Module {

        public static class V1 {

            public static class GET {

                public static class Res {
                    public static ModulesGETRes deserialize(String json) {
                        return JSONDeserializer.deserialize(json, ModulesGETRes.class);
                    }
                    public static String serialize(ModulesGETRes input) {
                        return JSONSerializer.serialize(input);
                    }
                }
            }

            public static class PUT {

                public static class Req {
                    public static ModulePUTReq deserialize(String json) {
                        return JSONDeserializer.deserialize(json, ModulePUTReq.class);
                    }

                    public static String serialize(ModulePUTReq input) {
                        return JSONSerializer.serialize(input);
                    }
                }

                public static class Res {
                    public static ModulePUTRes deserialize(String json) {
                        return JSONDeserializer.deserialize(json, ModulePUTRes.class);
                    }

                    public static String serialize(ModulePUTRes input) {
                        return JSONSerializer.serialize(input);
                    }
                }
            }

            public static class POST {

                public static class Req {
                    public static ModulePOSTReq deserialize(String json) {
                        return JSONDeserializer.deserialize(json, ModulePOSTReq.class);
                    }
                    public static String serialize(ModulePOSTReq input) {
                        return JSONSerializer.serialize(input);
                    }
                }

                public static class Res {
                    public static ModulePOSTRes deserialize(String json) {
                        return JSONDeserializer.deserialize(json, ModulePOSTRes.class);
                    }

                    public static String serialize(ModulePOSTRes input) {
                        return JSONSerializer.serialize(input);
                    }
                }
            }
            public static class DELETE {

                public static class Req {
                    public static ModuleDELETEReq deserialize(String json) {
                        return JSONDeserializer.deserialize(json, ModuleDELETEReq.class);
                    }
                    public static String serialize(ModuleDELETEReq input) {
                        return JSONSerializer.serialize(input);
                    }
                }

                public static class Res {
                    public static ModuleDELETERes deserialize(String json) {
                        return JSONDeserializer.deserialize(json, ModuleDELETERes.class);
                    }

                    public static String serialize(ModuleDELETERes input) {
                        return JSONSerializer.serialize(input);
                    }
                }
            }

        }
    }

    public static class HotSwap {

        public static class V1 {

            public static class POST {

                public static class Req {
                    public static HotSwapPOSTReq deserialize(String json) {
                        return JSONDeserializer.deserialize(json, HotSwapPOSTReq.class);
                    }

                    public static String serialize(HotSwapPOSTReq input) {
                        return JSONSerializer.serialize(input);
                    }
                }

                public static class Res {
                    public static HotSwapPOSTRes deserialize(String json) {
                        return JSONDeserializer.deserialize(json, HotSwapPOSTRes.class);
                    }

                    public static String serialize(HotSwapPOSTRes input) {
                        return JSONSerializer.serialize(input);
                    }
                }

                public static class ResNotFound {
                    public static HotSwapPOSTResNotFound deserialize(String json) {
                        return JSONDeserializer.deserialize(json, HotSwapPOSTResNotFound.class);
                    }

                    public static String serialize(HotSwapPOSTResNotFound input) {
                        return JSONSerializer.serialize(input);
                    }
                }
            }
        }
    }
}
