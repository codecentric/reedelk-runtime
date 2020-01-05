package com.reedelk.esb.services.scriptengine.evaluator;

public interface ValueProvider {

    <S> S empty();

    <S> S from(Object value);
}
