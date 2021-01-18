create table devices
(
    id             uuid primary key,
    circuit_id     uuid         not null unique,
    name           varchar(255) not null,
    is_online      boolean      not null default false,
    feedback_topic varchar(255) not null unique,
    querying_topic varchar(255) not null unique,
    alexa_slot_id  varchar(255),
    color_code     varchar(10)
);

create table actions
(
    id            uuid primary key,
    action_id     uuid unique  not null,
    name          varchar(255) not null,
    command_topic varchar(255) not null unique,
    alexa_intent  varchar(255) unique,
    value         float        not null,
    device_id     uuid         not null references devices (id)
)