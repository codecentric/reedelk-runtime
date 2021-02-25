package de.codecentric.reedelk.runtime.commons;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParser {

    private JsonParser() {
    }

    public static JSONObject from(String json) {
        return new JSONObject(json);
    }

    public interface Implementor {

        static String name() {
            return "implementor";
        }

        static String name(JSONObject object) {
            return object.getString(name());
        }

        static void name(String name, JSONObject object) {
            object.put(name(), name);
        }

        static String description() {
            return "description";
        }

        static String description(JSONObject object) {
            return object.getString(description());
        }

        static void description(String description, JSONObject object) {
            object.put(description(), description);
        }
    }

    public interface Component extends Implementor {

        static String ref() {
            return "ref";
        }

        static String ref(JSONObject object) {
            return object.getString(ref());
        }

        static void ref(String ref, JSONObject object) {
            object.put(ref(), ref);
        }
    }

    public interface Flow {

        static String id() {
            return "id";
        }

        static String id(JSONObject object) {
            return object.getString(id());
        }

        static void id(String id, JSONObject object) {
            object.put(id(), id);
        }

        static boolean hasId(JSONObject object) {
            return object.has(id());
        }

        static String flow() {
            return "flow";
        }

        static JSONArray flow(JSONObject object) {
            return object.getJSONArray(flow());
        }

        static void flow(JSONArray flow, JSONObject object) {
            object.put(flow(), flow);
        }

        static String description() {
            return "description";
        }

        static String description(JSONObject object) {
            return object.getString(description());
        }

        static void description(String description, JSONObject object) {
            object.put(description(), description);
        }

        static boolean hasDescription(JSONObject object) {
            return object.has(description());
        }

        static String title() {
            return "title";
        }

        static String title(JSONObject object) {
            return object.getString(title());
        }

        static void title(String title, JSONObject object) {
            object.put(title(), title);
        }

        static boolean hasTitle(JSONObject object) {
            return object.has(title());
        }
    }

    public interface Subflow {

        static String id() {
            return "id";
        }

        static String id(JSONObject object) {
            return object.getString(id());
        }

        static void id(String id, JSONObject object) {
            object.put(id(), id);
        }

        static boolean hasId(JSONObject object) {
            return object.has(id());
        }

        static String subflow() {
            return "subflow";
        }

        static JSONArray subflow(JSONObject object) {
            return object.getJSONArray("subflow");
        }

        static void subflow(JSONArray subflow, JSONObject object) {
            object.put(subflow(), subflow);
        }

        static String description() {
            return "description";
        }

        static String description(JSONObject object) {
            return object.getString(description());
        }

        static void description(String description, JSONObject object) {
            object.put(description(), description);
        }

        static boolean hasDescription(JSONObject object) {
            return object.has(description());
        }

        static String title() {
            return "title";
        }

        static String title(JSONObject object) {
            return object.getString(title());
        }

        static void title(String title, JSONObject object) {
            object.put(title(), title);
        }

        static boolean hasTitle(JSONObject object) {
            return object.has(title());
        }
    }

    public interface Config {

        static String id() {
            return "id";
        }

        static String id(JSONObject object) {
            return object.getString(id());
        }

        static void id(String id, JSONObject object) {
            object.put(id(), id);
        }

        static boolean hasId(JSONObject object) {
            return object.has(id());
        }

        static String title() {
            return "title";
        }

        static String title(JSONObject object) {
            return object.getString(title());
        }

        static void title(String title, JSONObject object) {
            object.put(title(), title);
        }
    }

    public interface Router extends Component {

        static String when() {
            return "when";
        }

        static JSONArray when(JSONObject object) {
            return object.getJSONArray(when());
        }

        static void when(JSONArray when, JSONObject object) {
            object.put(when(), when);
        }

        static String next() {
            return "next";
        }

        static JSONArray next(JSONObject object) {
            return object.getJSONArray(next());
        }

        static void next(JSONArray next, JSONObject object) {
            object.put(next(), next);
        }

        static String condition() {
            return "condition";
        }

        static String condition(JSONObject object) {
            return object.getString(condition());
        }

        static void condition(String condition, JSONObject object) {
            object.put(condition(), condition);
        }
    }

    public interface Fork extends Component {

        static String fork() {
            return "fork";
        }

        static JSONArray fork(JSONObject object) {
            return object.getJSONArray(fork());
        }

        static void fork(JSONArray fork, JSONObject object) {
            object.put(fork(), fork);
        }

        static String next() {
            return "next";
        }

        static JSONArray next(JSONObject object) {
            return object.getJSONArray(next());
        }

        static void next(JSONArray next, JSONObject object) {
            object.put(next(), next);
        }
    }

    public interface FlowReference extends Component {

        static String ref() {
            return "ref";
        }

        static String ref(JSONObject object) {
            return object.getString(ref());
        }

        static void ref(String ref, JSONObject object) {
            object.put(ref(), ref);
        }
    }

    public interface TryCatch extends Component {

        static String doTry() {
            return "try";
        }

        static JSONArray doTry(JSONObject object) {
            return object.getJSONArray(doTry());
        }

        static void doTry(JSONArray doTry, JSONObject object) {
            object.put(doTry(), doTry);
        }

        static String doCatch() {
            return "catch";
        }

        static JSONArray doCatch(JSONObject object) {
            return object.getJSONArray(doCatch());
        }

        static void doCatch(JSONArray doCatch, JSONObject object) {
            object.put(doCatch(), doCatch);
        }
    }

    public interface ForEach extends Component {

        static String next() {
            return "next";
        }

        static JSONArray next(JSONObject object) {
            return object.getJSONArray(next());
        }

        static void next(JSONArray doEach, JSONObject object) {
            object.put(next(), doEach);
        }

    }
}
