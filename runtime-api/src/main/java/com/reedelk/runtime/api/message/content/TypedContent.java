package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;

@AutocompleteType(
        description = "A typed content contains information about the payload " +
                "which encapsulates. Information provided by type are payload type " +
                "(e.g String, Object, Collection), mime type (e.g text/plain, image/jpeg), " +
                "and the actual content data.")
public interface TypedContent<ItemType, PayloadType> extends Serializable {

    @AutocompleteItem(
            signature = "type()",
            example = "message.content().type()",
            description = "Returns the data type of the content.")
    Class<ItemType> type();

    default Class<ItemType> getType() {
        return type();
    }

    @AutocompleteItem(
            signature = "mimeType()",
            example = "message.content().mimeType()",
            description = "Returns the mime type of the content.")
    MimeType mimeType();

    default MimeType getMimeType() {
        return mimeType();
    }

    @AutocompleteItem(
            returnType = Object.class,
            signature = "data()",
            example = "message.content().data()",
            description = "Returns the actual data which could be could be a text, " +
                    "a byte array, a collection and so on depending on the component which generated it.")
    PayloadType data();

    default PayloadType getData() {
        return data();
    }

    TypedPublisher<ItemType> stream();

    default TypedPublisher<ItemType> getStream() {
        return stream();
    }

    @AutocompleteItem(
            signature = "isStream()",
            example = "message.content().isStream()",
            description = "Returns true if this message is a stream, false otherwise.")
    boolean isStream();

    /**
     * Consumes the stream payload if it has not been consumed yet. This method might be useful
     * to call before cloning the message. E.g the fork component uses this method before
     * invoking fork branches with a copy of the message.
     */
    @AutocompleteItem(
            signature = "consume()",
            example = "message.content().consume()",
            description = "Consumes the stream of this message by loading the entire " +
                    "stream content in memory.")
    void consume();
}
