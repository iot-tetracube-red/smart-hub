drop schema public cascade;

create schema public;

create table devices
(
    id             uuid primary key,
    name           varchar(255) not null,
    is_online      boolean      not null default false,
    feedback_topic varchar(255) not null unique,
    color_code     varchar(10)
);


create table features
(
    id                  uuid primary key,
    name                varchar(255) not null,
    feature_type        varchar(255) not null,
    current_value       float        not null,
    is_running          bool default false,
    source_type         varchar(100) null,
    source_reference_id varchar(255) null,
    device_id           uuid         not null references devices (id)
);


create table actions
(
    id            uuid primary key,
    trigger_topic varchar(255) not null unique,
    name          varchar(255) not null,
    feature_id    uuid         not null references features (id)
);