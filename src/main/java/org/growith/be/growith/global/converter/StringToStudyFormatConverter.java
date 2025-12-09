package org.growith.be.growith.global.converter;

import org.growith.be.growith.domain.study.entity.enums.StudyFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStudyFormatConverter implements Converter<String, StudyFormat> {
    @Override
    public StudyFormat convert(String source) {
        return StudyFormat.from(source);
    }
}
