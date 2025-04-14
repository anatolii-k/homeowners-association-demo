package anatolii.k.hoa.common.application;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AttributesValidation {
    private final Map<String, List<AttributeValidator<?>>> validators = new TreeMap<>();

    public <T> void registerValidator(String attributeName, AttributeValidator<T> validator){
        validators.computeIfAbsent(attributeName, value -> new LinkedList<>())
                  .add(validator);
    }

    public <T> void validate(String attributeName, T value){
        var attributeValidators = validators.get(attributeName);
        if(attributeValidators == null){
            return;
        }
        attributeValidators.forEach(
                validator -> ((AttributeValidator<T>)validator).validate(attributeName,value) );
    }
}
