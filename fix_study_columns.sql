-- study 테이블의 enum 컬럼 크기 수정
ALTER TABLE study MODIFY COLUMN study_style_category VARCHAR(50);
ALTER TABLE study MODIFY COLUMN contact_type VARCHAR(50);
ALTER TABLE study MODIFY COLUMN study_format VARCHAR(50);
ALTER TABLE study MODIFY COLUMN study_status VARCHAR(50);

-- rule 테이블의 enum 컬럼 크기 수정 (있다면)
ALTER TABLE rule MODIFY COLUMN rule_category VARCHAR(50);
