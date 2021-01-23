drop schema public cascade;
create schema public ;

create table devices
(
    id             uuid primary key,
    circuit_id     uuid         not null unique,
    name           varchar(255) not null,
    is_online      boolean      not null default false,
    feedback_topic varchar(255) not null unique,
    alexa_slot_id  varchar(255),
    color_code     varchar(10)
);

create table features
(
    id            uuid primary key,
    feature_id    uuid unique  not null,
    command_topic varchar(255) not null unique,
    query_topic   varchar(255) not null unique,
    name          varchar(255) not null,
    feature_type  varchar(255) not null,
    current_value float        not null,
    circuit_id    uuid         not null references devices (id)
);

create table actions
(
    id           uuid primary key,
    action_id    uuid unique  not null,
    name         varchar(255) not null,
    alexa_intent varchar(255) unique,
    feature_id   uuid         not null references features (id)
);