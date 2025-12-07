package org.growith.be.growith.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QueryDslConfig {

    private final EntityManager em;

    @Bean
    JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }
}
