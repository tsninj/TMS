CREATE TABLE workflows (
                           id VARCHAR(255) PRIMARY KEY,
                           department_id VARCHAR(255) NOT NULL ,
                           title VARCHAR(255) NOT NULL,
                           status VARCHAR(50) NOT NULL
);

CREATE TABLE workflow_stages (
                                 id VARCHAR(255) PRIMARY KEY,
                                 workflow_id VARCHAR(255) NOT NULL REFERENCES workflows(id) ON DELETE CASCADE,
                                 name VARCHAR(100) NOT NULL,
                                 start_date DATE NOT NULL,
                                 end_date DATE NOT NULL,
                                 weight_percent NUMERIC(5,2) NOT NULL,
                                 status VARCHAR(50) NOT NULL,
                                 stage_order INT NOT NULL
);
CREATE TABLE stage_criteria (
                                id VARCHAR(100) PRIMARY KEY,
                                stage_id VARCHAR(100) NOT NULL REFERENCES workflow_stages(id) ON DELETE CASCADE,
                                name VARCHAR(255) NOT NULL,
                                max_score NUMERIC(5,2) NOT NULL,
                                description TEXT
);
CREATE TABLE workflow_stage_evaluator_roles (
                                                id VARCHAR(100) PRIMARY KEY,
                                                stage_id VARCHAR(100) NOT NULL REFERENCES workflow_stages(id) ON DELETE CASCADE,
                                                evaluator_role VARCHAR(50) NOT NULL
);