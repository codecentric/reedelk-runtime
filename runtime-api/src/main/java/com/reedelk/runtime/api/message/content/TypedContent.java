package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;

@AutocompleteType
public interface TypedContent<ItemType, PayloadType> extends Serializable {

    @AutocompleteItem(replaceValue = "type()",
            description = "Returns the type of the content.")
    Class<ItemType> type();

    default Class<ItemType> getType() {
        return type();
    }

    @AutocompleteItem(replaceValue = "mimeType()",
            description = "Returns the mime type of the content.")
    MimeType mimeType();

    default MimeType getMimeType() {
        return mimeType();
    }

    @AutocompleteItem(returnType = Object.class, replaceValue = "data()",
            description = "Returns data of this message.")
    PayloadType data();

    default PayloadType getData() {
        return data();
    }

    TypedPublisher<ItemType> stream();

    default TypedPublisher<ItemType> getStream() {
        return stream();
    }

    @AutocompleteItem(replaceValue = "isStream()",
            description = "Returns true if this message is a stream, false otherwise.")
    boolean isStream();

    @AutocompleteItem(replaceValue = "isConsumed()",
            description = "Returns true if this message stream has been consumed, false otherwise.")
    boolean isConsumed();

    /**
     * Consumes the stream payload if it has not been consumed yet. This method might be useful
     * to call before cloning the message. E.g the fork component uses this method before
     * invoking fork branches with a copy of the message.
     */
    @AutocompleteItem(replaceValue = "consume()",
            description = "Consumes the payload of this message by loading the entire stream content into memory.")
    void consume();
}
