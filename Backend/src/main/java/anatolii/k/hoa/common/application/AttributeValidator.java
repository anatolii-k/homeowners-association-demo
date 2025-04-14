package anatolii.k.hoa.common.application;

@FunctionalInterface
public interface AttributeValidator<T> {
    void validate(String attributeName, T value);
}
