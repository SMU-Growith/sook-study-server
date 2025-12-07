package org.growith.be.growith.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {
    @PersistenceContext
    EntityManager em;

    @Bean
    JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }
}
