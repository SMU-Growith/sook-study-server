package org.growith.be.growith.global.config;

import lombok.RequiredArgsConstructor;
import org.growith.be.growith.global.annotation.AuthenticatedMemberResolver;
import org.growith.be.growith.global.converter.StringToStudyFormatConverter;
import org.growith.be.growith.global.converter.StringToStudyStyleCategoryConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticatedMemberResolver authenticatedMemberResolver;
    private final StringToStudyFormatConverter stringToStudyFormatConverter;
    private final StringToStudyStyleCategoryConverter stringToStudyStyleCategoryConverter;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedMemberResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToStudyFormatConverter);
        registry.addConverter(stringToStudyStyleCategoryConverter);
    }
}
