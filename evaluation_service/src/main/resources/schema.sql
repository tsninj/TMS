create table evaluations (
                             id varchar(255) primary key,
                             thesis_id varchar(255) not null,
                             workflow_id varchar(255) not null,
                             stage_id varchar(255) not null,
                             stage_name varchar(255) not null,
                             stage_max_point numeric(10,2) not null,
                             committee_id varchar(255),
                             evaluator_id varchar(255),
                             status varchar(50) not null
);

create table criterion_assessments (
                                       id varchar(255) primary key,
                                       evaluation_id varchar(255) not null references evaluations(id) on delete cascade,
                                       criterion_id varchar(255) not null,
                                       criterion_name varchar(255) not null,
                                       max_point numeric(10,2) not null,
                                       achieved_percent integer not null check (achieved_percent between 0 and 100),
                                       weighted_score numeric(10,2) not null
);
