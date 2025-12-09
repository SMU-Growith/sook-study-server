package org.growith.be.growith.global.converter;

import org.growith.be.growith.domain.study.entity.enums.StudyStyleCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStudyStyleCategoryConverter implements Converter<String, StudyStyleCategory> {
    @Override
    public StudyStyleCategory convert(String source) {
        return StudyStyleCategory.from(source);
    }
}
