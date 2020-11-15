CREATE TABLE devices
(
    id            uuid primary key unique,
    name          varchar(255) not null,
    circuit_id    uuid         not null unique,
    is_online     bool         not null default false,
    alexa_slot_id varchar(255) unique
);
