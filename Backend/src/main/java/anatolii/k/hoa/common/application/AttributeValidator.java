package anatolii.k.hoa.common.application;

import anatolii.k.hoa.common.domain.CommonException;

@FunctionalInterface
public interface AttributeValidator<T> {
    void validate(String attributeName, T value);
}
