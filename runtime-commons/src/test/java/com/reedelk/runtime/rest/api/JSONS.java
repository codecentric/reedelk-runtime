package com.reedelk.runtime.rest.api;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public enum JSONS {

    HealthGETRes {
        @Override
        public String path() {
            return "/com.reedelk.runtime.rest.api.json/HealthGETRes.json";
        }
    },

    ModulesGETRes {
        @Override
        public String path() {
            return "/com.reedelk.runtime.rest.api.json/ModulesGETRes.json";
        }
    },

    ModulePUTReq {
        @Override
        public String path() {
            return "/com.reedelk.runtime.rest.api.json/ModulePUTReq.json";
        }
    },

    ModulePUTRes {
        @Override
        public String path() {
            return "/com.reedelk.runtime.rest.api.json/ModulePUTRes.json";
        }
    },

    ModulePOSTReq {
        @Override
        public String path() {
            return "/com.reedelk.runtime.rest.api.json/ModulePOSTReq.json";
        }
    },

    ModulePOSTRes {
        @Override
        public String path() {
            return "/com.reedelk.runtime.rest.api.json/ModulePOSTRes.json";
        }
    },

    ModuleDELETEReq {
        @Override
        public String path() {
            return "/com.reedelk.runtime.rest.api.json/ModuleDELETEReq.json";
        }
    },

    ModuleDELETERes {
        @Override
        public String path() {
            return "/com.reedelk.runtime.rest.api.json/ModuleDELETERes.json";
        }
    },

    HotSwapPOSTReq {
        @Override
        public String path() {
            return "/com.reedelk.runtime.rest.api.json/HotSwapPOSTReq.json";
        }
    },

    HotSwapPOSTRes {
        @Override
        public String path() {
            return "/com.reedelk.runtime.rest.api.json/HotSwapPOSTRes.json";
        }
    },

    HotSwapPOSTResNotFound {
        @Override
        public String path() {
            return "/com.reedelk.runtime.rest.api.json/HotSwapPOSTResNotFound.json";
        }
    };


    public URL url() {
        return JSONS.class.getResource(path());
    }

    public abstract String path();

    public String string() {
        try (Scanner scanner = new Scanner(url().openStream(), UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            throw new RuntimeException("String from URI could not be read.", e);
        }
    }
}
