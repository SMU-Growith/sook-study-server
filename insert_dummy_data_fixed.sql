-- ========================================
-- Growith RDS Dummy Data Insert Script (Fixed)
-- ========================================

-- 1. Insert Users (더미 사용자 데이터)
INSERT INTO `user` (login_id, email, password, nick_name, major, student_status, phone_number, user_role, is_notice, is_deleted, created_at, updated_at)
VALUES
('minseo', 'minseo@smu.ac.kr', '$2a$10$dummypasswordhash1', '민서송이', 'DESIGN', 'ENROLLED', '010-1111-1111', 'USER', TRUE, NULL, NOW(), NOW()),
('coding_king', 'coding@smu.ac.kr', '$2a$10$dummypasswordhash2', '코딩왕', 'COMPUTER_SCIENCE', 'ENROLLED', '010-2222-2222', 'USER', TRUE, NULL, NOW(), NOW()),
('english_master', 'english@smu.ac.kr', '$2a$10$dummypasswordhash3', '영어고수', 'ENGLISH', 'ENROLLED', '010-3333-3333', 'USER', TRUE, NULL, NOW(), NOW()),
('cs_student', 'cs@smu.ac.kr', '$2a$10$dummypasswordhash4', '컴공눈송', 'COMPUTER_SCIENCE', 'ENROLLED', '010-4444-4444', 'USER', TRUE, NULL, NOW(), NOW()),
('design_master', 'design@smu.ac.kr', '$2a$10$dummypasswordhash5', '디자인장인', 'DESIGN', 'ENROLLED', '010-5555-5555', 'USER', TRUE, NULL, NOW(), NOW()),
('coding_genius', 'genius@smu.ac.kr', '$2a$10$dummypasswordhash6', '코딩천재', 'COMPUTER_SCIENCE', 'ENROLLED', '010-6666-6666', 'USER', TRUE, NULL, NOW(), NOW()),
('pm_wannabe', 'pm@smu.ac.kr', '$2a$10$dummypasswordhash7', 'PM지망생', 'BUSINESS', 'ENROLLED', '010-7777-7777', 'USER', TRUE, NULL, NOW(), NOW()),
('international', 'intl@smu.ac.kr', '$2a$10$dummypasswordhash8', '유학생', 'ENGLISH', 'ENROLLED', '010-8888-8888', 'USER', TRUE, NULL, NOW(), NOW()),
('interview_lover', 'interview@smu.ac.kr', '$2a$10$dummypasswordhash9', '면접러버', 'COMPUTER_SCIENCE', 'ENROLLED', '010-9999-9999', 'USER', TRUE, NULL, NOW(), NOW()),
('frontend_song', 'frontend@smu.ac.kr', '$2a$10$dummypasswordhash10', '프론트송', 'COMPUTER_SCIENCE', 'ENROLLED', '010-1010-1010', 'USER', TRUE, NULL, NOW(), NOW()),
('coding_song', 'coding2@smu.ac.kr', '$2a$10$dummypasswordhash11', '코딩송', 'COMPUTER_SCIENCE', 'ENROLLED', '010-1111-2222', 'USER', TRUE, NULL, NOW(), NOW()),
('state_song', 'state@smu.ac.kr', '$2a$10$dummypasswordhash12', '상태송', 'COMPUTER_SCIENCE', 'ENROLLED', '010-1212-1212', 'USER', TRUE, NULL, NOW(), NOW()),
('debug_song', 'debug@smu.ac.kr', '$2a$10$dummypasswordhash13', '디버깅송', 'COMPUTER_SCIENCE', 'ENROLLED', '010-1313-1313', 'USER', TRUE, NULL, NOW(), NOW());

-- 2. Insert Study Fields (스터디 분야) - 실제 컬럼명 사용
INSERT INTO study_field (name, level, sort_order, parent_id, created_at, updated_at)
VALUES
('디자인', 1, 1, NULL, NOW(), NOW()),
('코딩', 1, 2, NULL, NOW(), NOW()),
('어학', 1, 3, NULL, NOW(), NOW()),
('비즈니스', 1, 4, NULL, NOW(), NOW());

-- 3. Insert Studies (스터디 데이터)
INSERT INTO study (title, description, study_status, contact_type, url, is_recruiting, study_field_id, user_id, study_format, study_style_category, scrap_count, created_at, updated_at)
VALUES
-- Study 1: Figma 스터디
('Figma 스터디하실 분 모집합니다!', 'Figma를 활용한 UI/UX 디자인 스터디입니다. 초보자도 환영합니다!', 'RECRUITING', 'KAKAO', 'https://open.kakao.com/figma', TRUE, 1, 1, 'BOTH', 'SYSTEMATIC', 12, NOW(), NOW()),

-- Study 2: React 스터디
('React 실력 키우실 분! 초보도 환영!', 'React 기초부터 고급까지 함께 공부합니다.', 'RECRUITING', 'KAKAO', 'https://open.kakao.com/react', TRUE, 2, 2, 'ONLINE', 'FREE', 25, NOW(), NOW()),

-- Study 3: 토익 스터디
('토익 900점 목표로 같이 공부하실 분 모집합니다.', '토익 고득점을 목표로 하는 스터디입니다.', 'COMPLETED', 'EMAIL', 'english@smu.ac.kr', FALSE, 3, 3, 'OFFLINE', 'SYSTEMATIC', 8, NOW(), NOW()),

-- Study 4: CS 스터디
('CS 스터디 취준생 모여라', '컴퓨터 과학 기초를 다지는 스터디입니다.', 'RECRUITING', 'KAKAO', 'https://open.kakao.com/cs', TRUE, 2, 4, 'OFFLINE', 'FREE', 30, NOW(), NOW()),

-- Study 5: UX/UI 포트폴리오
('UX/UI 포트폴리오 만들 디자이너 구함', '포트폴리오 제작을 위한 협업 스터디입니다.', 'RECRUITING', 'KAKAO', 'https://open.kakao.com/uxui', TRUE, 1, 5, 'OFFLINE', 'COLLABORATIVE', 18, NOW(), NOW()),

-- Study 6: 알고리즘
('알고리즘 문제풀이 코테 박살내기', '코딩테스트 대비 알고리즘 스터디입니다.', 'RECRUITING', 'KAKAO', 'https://open.kakao.com/algo', TRUE, 2, 6, 'ONLINE', 'SYSTEMATIC', 40, NOW(), NOW()),

-- Study 7: 사이드 프로젝트
('사이드 플젝 하실 분 기획자/코딩자', '실전 프로젝트를 함께 진행합니다.', 'COMPLETED', 'KAKAO', 'https://open.kakao.com/project', FALSE, 2, 7, 'ONLINE', 'FREE', 22, NOW(), NOW()),

-- Study 8: 영어 회화
('영어 회화 스터디 주 2회 진행', '영어 회화 실력 향상을 위한 스터디입니다.', 'RECRUITING', 'EMAIL', 'intl@smu.ac.kr', TRUE, 3, 8, 'OFFLINE', 'SYSTEMATIC', 15, NOW(), NOW()),

-- Study 9: 프론트엔드 면접
('프론트엔드 취준 면접 대비 스터디', '프론트엔드 개발자 면접을 준비합니다.', 'RECRUITING', 'KAKAO', 'https://open.kakao.com/frontend', TRUE, 2, 9, 'ONLINE', 'SYSTEMATIC', 27, NOW(), NOW());

-- 4. Insert Study Sessions (스터디 세션 데이터)
INSERT INTO study_session (number, is_submitted, is_active, title, study_id, created_at, updated_at)
VALUES
-- React 고급 훅 분석하기 (Study 2)
(1, FALSE, TRUE, 'React 고급 훅 분석하기', 2, NOW(), NOW()),

-- React 렌더링 최적화 실습 (Study 2)
(2, FALSE, FALSE, 'React 렌더링 최적화 실습', 2, NOW(), NOW()),

-- 상태관리 라이브러리 비교하기 (Study 2)
(3, FALSE, TRUE, '상태관리 라이브러리 비교하기', 2, NOW(), NOW()),

-- Custom Hook 직접 만들어보기 (Study 2)
(4, FALSE, FALSE, 'Custom Hook 직접 만들어보기', 2, NOW(), NOW()),

-- React Query 실전 활용법 (Study 2)
(5, FALSE, TRUE, 'React Query 실전 활용법', 2, NOW(), NOW()),

-- 컴포넌트 리렌더링 분석하기 (Study 2)
(6, FALSE, FALSE, '컴포넌트 리렌더링 분석하기', 2, NOW(), NOW()),

-- Form 관리와 Zod 검증 실습 (Study 2)
(7, FALSE, TRUE, 'Form 관리와 Zod 검증 실습', 2, NOW(), NOW()),

-- React + Zustand 구조 잡기 (Study 2)
(8, FALSE, FALSE, 'React + Zustand 구조 잡기', 2, NOW(), NOW()),

-- React 최적화 패턴 익히기 (Study 2)
(9, FALSE, TRUE, 'React 최적화 패턴 익히기', 2, NOW(), NOW());

-- 5. Insert Study Journals (스터디 일지 데이터)
INSERT INTO study_journal (title, content, url, session_id, user_id, created_at, updated_at)
VALUES
-- Journal 1
('React 렌더링 최적화 기본기 다지기', 
 'React의 렌더링 원리를 다시 학습하고, Virtual DOM 비교 과정과 리렌더링 트리거 조건을 정리했습니다.', 
 'https://react.dev/learn/render-and-commit', 
 2, 10, NOW(), NOW()),

-- Journal 2
('React.memo & useMemo 실전 적용해보기', 
 '불필요한 렌더링을 줄이기 위해 React.memo와 useMemo를 여러 예제로 실습하고, 언제 써야 할지 기준을 정리했습니다.', 
 'https://react.dev/reference/react/useMemo', 
 2, 11, NOW(), NOW()),

-- Journal 3
('React 상태관리 최적화 (Zustand + React Query)', 
 'Zustand의 부분 구독 패턴과 React Query의 캐싱 전략을 활용해, 상태 변경이 최소한의 컴포넌트만 렌더링되도록 개선했습니다.', 
 'pmndrs/zustand.pdf', 
 3, 12, NOW(), NOW()),

-- Journal 4
('성능 분석 도구로 렌더링 문제 찾기', 
 'React Profiler와 Chrome Performance 패널을 활용해 실제 렌더링 문제를 찾고, Fiber 트리를 시각적으로 분석했습니다.', 
 'pmndrs/zustand.pdf', 
 2, 13, NOW(), NOW());

-- 6. Insert Study Applications (스터디 신청 데이터)
INSERT INTO study_application (nickname, student_status, major, phone_number, motivation, application_status, user_id, study_id, created_at, updated_at)
VALUES
-- Applications for Study 1 (Figma)
('프론트송', 'ENROLLED', 'COMPUTER_SCIENCE', '010-1010-1010', 'Figma를 배워서 디자이너와 협업을 더 잘하고 싶습니다!', 'APPROVED', 10, 1, NOW(), NOW()),
('코딩송', 'ENROLLED', 'COMPUTER_SCIENCE', '010-1111-2222', 'UI/UX에 관심이 많아서 신청합니다.', 'APPROVED', 11, 1, NOW(), NOW()),

-- Applications for Study 2 (React)
('상태송', 'ENROLLED', 'COMPUTER_SCIENCE', '010-1212-1212', 'React를 깊이 있게 공부하고 싶습니다.', 'APPROVED', 12, 2, NOW(), NOW()),
('디버깅송', 'ENROLLED', 'COMPUTER_SCIENCE', '010-1313-1313', '실무에서 사용할 React 스킬을 배우고 싶어요.', 'APPROVED', 13, 2, NOW(), NOW()),

-- Applications for Study 4 (CS)
('프론트송', 'ENROLLED', 'COMPUTER_SCIENCE', '010-1010-1010', 'CS 기초를 탄탄히 하고 싶습니다.', 'PENDING', 10, 4, NOW(), NOW()),

-- Applications for Study 6 (알고리즘)
('코딩송', 'ENROLLED', 'COMPUTER_SCIENCE', '010-1111-2222', '코딩테스트를 준비하고 있습니다.', 'APPROVED', 11, 6, NOW(), NOW()),
('상태송', 'ENROLLED', 'COMPUTER_SCIENCE', '010-1212-1212', '알고리즘 실력을 키우고 싶어요.', 'APPROVED', 12, 6, NOW(), NOW());

-- ========================================
-- Verification Queries
-- ========================================

-- Check inserted data
SELECT 'Users' as table_name, COUNT(*) as count FROM `user`
UNION ALL
SELECT 'Study Fields', COUNT(*) FROM study_field
UNION ALL
SELECT 'Studies', COUNT(*) FROM study
UNION ALL
SELECT 'Sessions', COUNT(*) FROM study_session
UNION ALL
SELECT 'Journals', COUNT(*) FROM study_journal
UNION ALL
SELECT 'Applications', COUNT(*) FROM study_application;

-- View sample data
SELECT id, nick_name, email FROM `user` LIMIT 5;
SELECT study_field_id, name, level FROM study_field;
SELECT study_id, title, study_status FROM study LIMIT 5;
SELECT study_session_id, number, title FROM study_session LIMIT 5;
SELECT id, title FROM study_journal LIMIT 5;
SELECT study_application_id, nickname, application_status FROM study_application LIMIT 5;
