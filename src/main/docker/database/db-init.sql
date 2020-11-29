drop schema if exists public cascade;
create schema public;

CREATE TABLE devices
(
    id            uuid primary key unique,
    name          varchar(255) not null,
    client_name   varchar(255) not null unique,
    circuit_id    uuid         not null unique,
    is_online     bool         not null default false,
    alexa_slot_id varchar(255) unique
);

CREATE TABLE actions
(
    id           uuid primary key unique,
    device_id    uuid references devices (id),
    name         varchar(255) not null unique,
    hardware_id  uuid         not null,
    topic        varchar(255) not null unique,
    alexa_intent varchar(255)
);
