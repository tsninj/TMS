CREATE DATABASE user_service;
CREATE DATABASE thesis_service;
CREATE DATABASE workflow_service;
CREATE DATABASE evaluation_service;
CREATE DATABASE grading_service;
CREATE DATABASE notification_service;
CREATE DATABASE report_service;

\connect user_service

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    department_id VARCHAR(255),
    system_role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS students (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    student_id VARCHAR(255) NOT NULL UNIQUE,
    major VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_students_user_id ON students(user_id);

CREATE TABLE IF NOT EXISTS teachers (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    position VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_teachers_user_id ON teachers(user_id);

CREATE TABLE IF NOT EXISTS departments (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    department_name VARCHAR(255) NOT NULL
);

\connect thesis_service

CREATE TABLE IF NOT EXISTS theses (
    id VARCHAR(255) PRIMARY KEY,
    student_id VARCHAR(255) NOT NULL,
    supervisor_id VARCHAR(255),
    committee_id VARCHAR(255),
    title_mn VARCHAR(500) NOT NULL,
    title_en VARCHAR(500) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_theses_student_id ON theses(student_id);

\connect workflow_service

CREATE TABLE IF NOT EXISTS workflows (
    id VARCHAR(255) PRIMARY KEY,
    department_id VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS workflow_stages (
    id VARCHAR(255) PRIMARY KEY,
    workflow_id VARCHAR(255) NOT NULL REFERENCES workflows(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    weight_percent NUMERIC(5,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    stage_order INT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_workflow_stages_workflow_id ON workflow_stages(workflow_id);

\connect evaluation_service

CREATE TABLE IF NOT EXISTS evaluations (
    id VARCHAR(255) PRIMARY KEY,
    thesis_id VARCHAR(255) NOT NULL,
    workflow_id VARCHAR(255) NOT NULL,
    stage_id VARCHAR(255) NOT NULL,
    stage_name VARCHAR(255) NOT NULL,
    stage_max_point NUMERIC(10,2) NOT NULL,
    committee_id VARCHAR(255),
    evaluator_id VARCHAR(255),
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS criterion_assessments (
    id VARCHAR(255) PRIMARY KEY,
    evaluation_id VARCHAR(255) NOT NULL REFERENCES evaluations(id) ON DELETE CASCADE,
    criterion_id VARCHAR(255) NOT NULL,
    criterion_name VARCHAR(255) NOT NULL,
    max_point NUMERIC(10,2) NOT NULL,
    achieved_percent INTEGER NOT NULL CHECK (achieved_percent BETWEEN 0 AND 100),
    weighted_score NUMERIC(10,2) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_criterion_assessments_evaluation_id
    ON criterion_assessments(evaluation_id);

\connect grading_service

CREATE TABLE IF NOT EXISTS grades (
    id VARCHAR(255) PRIMARY KEY,
    thesis_id VARCHAR(255) NOT NULL,
    student_id VARCHAR(255) NOT NULL,
    workflow_id VARCHAR(255) NOT NULL,
    resolution_id VARCHAR(255),
    total_score DOUBLE PRECISION,
    status VARCHAR(50) NOT NULL,
    calculated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_grades_thesis_id ON grades(thesis_id);
CREATE INDEX IF NOT EXISTS idx_grades_workflow_id ON grades(workflow_id);
CREATE INDEX IF NOT EXISTS idx_grades_resolution_id ON grades(resolution_id);

CREATE TABLE IF NOT EXISTS resolutions (
    id VARCHAR(255) PRIMARY KEY,
    workflow_id VARCHAR(255) NOT NULL,
    resolution_number VARCHAR(255) NOT NULL,
    total_students INTEGER NOT NULL,
    passed_count INTEGER NOT NULL,
    failed_count INTEGER NOT NULL,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_resolutions_workflow_id ON resolutions(workflow_id);

\connect notification_service

CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(100) NOT NULL,
    channel VARCHAR(100),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_notifications_user_id ON notifications(user_id);

\connect report_service

-- report_service currently has no persistence entity. The database is created
-- so the service can start with its existing R2DBC configuration.
