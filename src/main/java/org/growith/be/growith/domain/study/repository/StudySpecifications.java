package org.growith.be.growith.domain.study.repository;

import org.growith.be.growith.domain.study.entity.Study;
import org.growith.be.growith.domain.study.entity.enums.StudyStatus;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class StudySpecifications {
    public static Specification<Study> searchSpec(
            List<String> fields,
            List<String> formats,
            List<String> styles,
            String status,
            String keyword
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 분야 OR
            if (fields != null && !fields.isEmpty()) {
                predicates.add(root.get("studyField").get("name").in(fields));
            }
            // 진행방식 OR
            if (formats != null && !formats.isEmpty()) {
                predicates.add(root.get("format").in(formats));
            }
            // 모집상태
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("studyStatus"), StudyStatus.valueOf(status.toUpperCase())));
            }
            // 검색어 (제목/내용)
            if (keyword != null && !keyword.isBlank()) {
                Predicate titleLike = cb.like(root.get("title"), "%" + keyword + "%");
                Predicate descLike = cb.like(root.get("description"), "%" + keyword + "%");
                predicates.add(cb.or(titleLike, descLike));
            }

            query.distinct(true); // 중복 제거
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

