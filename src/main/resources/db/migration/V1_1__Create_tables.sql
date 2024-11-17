-- auto-generated definition
create table channel
(
    id   bigint not null
        primary key,
    name varchar(255)
);

alter table channel
    owner to postgres;

-- auto-generated definition
create table channel_roles
(
    channel_id bigint not null
        constraint fkjc0nsg1kj0x0jvtxrclljr34j
            references channel,
    roles      varchar(255)
);

alter table channel_roles
    owner to postgres;

-- auto-generated definition
create sequence hibernate_sequence;

alter sequence hibernate_sequence owner to postgres;

