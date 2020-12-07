create table devices
(
    id            uuid primary key,
    circuit_id    uuid         not null unique,
    name          varchar(255) not null,
    is_online     boolean      not null default false,
    alexa_slot_id varchar(255),
    client_name   varchar(255)
);

create table actions
(
    id            uuid primary key,
    action_id     uuid unique  not null,
    name          varchar(255) not null,
    is_default    boolean      not null default false,
    publish_topic varchar(255) not null unique,
    alexa_intent  varchar(255) unique,
    device_id     uuid         not null references devices (id)
)