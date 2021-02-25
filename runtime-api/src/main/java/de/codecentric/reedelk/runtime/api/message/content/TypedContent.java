package de.codecentric.reedelk.runtime.api.message.content;

import de.codecentric.reedelk.runtime.api.annotation.Type;
import de.codecentric.reedelk.runtime.api.annotation.TypeFunction;
import de.codecentric.reedelk.runtime.api.message.MessagePayload;

import java.io.Serializable;

/**
 * The type refers to the type of the resolved data.
 * For example, the resolved data for StringContent is string, for ListContent is List,
 * ObjectContent is the object type and so on. The stream type refers to the data generated by the stream. For List
 * content the type is the type of the object in the List.
 * @param <T> the type of the resolved data (not streamed).
 * @param <StreamType> the type of each element of the stream.
 */
@Type(description = "A typed content contains information about the payload " +
                "which encapsulates. Information provided by type are payload type " +
                "(e.g String, Object, Collection), mime type (e.g text/plain, image/jpeg), " +
                "and the actual content data.")
public interface TypedContent<T, StreamType> extends Serializable {

    @TypeFunction(
            signature = "type()",
            example = "message.content().type()",
            description = "Returns the data type of the content.")
    Class<T> type();

    default Class<T> getType() {
        return type();
    }

    @TypeFunction(
            signature = "streamType()",
            example = "message.content().streamType()",
            description = "Returns the data type of the stream generated by this content.")
    Class<StreamType> streamType();

    default Class<StreamType> getStreamType() {
        return streamType();
    }

    @TypeFunction(
            signature = "mimeType()",
            example = "message.content().mimeType()",
            description = "Returns the mime type of the content.")
    MimeType mimeType();

    default MimeType getMimeType() {
        return mimeType();
    }

    @TypeFunction(
            returnType = MessagePayload.class,
            signature = "data()",
            example = "message.content().data()",
            description = "Returns the actual data which could be could be a text, " +
                    "a byte array, a collection and so on depending on the component which generated it.")
    T data();

    default T getData() {
        return data();
    }

    TypedPublisher<StreamType> stream();

    default TypedPublisher<StreamType> getStream() {
        return stream();
    }

    @TypeFunction(
            signature = "isStream()",
            example = "message.content().isStream()",
            description = "Returns true if this message is a stream, false otherwise.")
    boolean isStream();

    /**
     * Consumes the stream payload if it has not been consumed yet. This method might be useful
     * to call before cloning the message. E.g the fork component uses this method before
     * invoking fork branches with a copy of the message.
     */
    @TypeFunction(
            signature = "consume()",
            example = "message.content().consume()",
            description = "Consumes the stream of this message by loading the entire " +
                    "stream content in memory.")
    void consume();
}