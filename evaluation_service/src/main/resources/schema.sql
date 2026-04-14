create table evaluations (
                             id uuid primary key,
                             thesis_id uuid not null,
                             workflow_id uuid not null,
                             stage_id uuid not null,
                             stage_name varchar(255) not null,
                             stage_max_point numeric(10,2) not null,
                             committee_id uuid,
                             evaluator_id uuid,
                             status varchar(50) not null
);

create table criterion_assessments (
                                       id uuid primary key,
                                       evaluation_id uuid not null references evaluations(id) on delete cascade,
                                       criterion_id uuid not null,
                                       criterion_name varchar(255) not null,
                                       max_point numeric(10,2) not null,
                                       achieved_percent integer not null check (achieved_percent between 0 and 100),
                                       weighted_score numeric(10,2) not null
);