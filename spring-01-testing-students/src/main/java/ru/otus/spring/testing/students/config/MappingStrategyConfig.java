package ru.otus.spring.testing.students.config;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;

public class MappingStrategyConfig<T extends Class<T>> extends HeaderColumnNameMappingStrategy<T> {

    public MappingStrategyConfig(T aClass){
        this.setType(aClass);
    }

}
