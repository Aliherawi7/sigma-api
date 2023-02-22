package com.herawi.sigma.services;
import com.herawi.sigma.constants.Gender;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumTypeConverter implements Converter<String, Gender> {

    @Override
    public Gender convert(String source) {
        return Gender.decode(source.toUpperCase());
    }

}
