--liquibase formatted sql
create table if not exists link (
    id bigint generated always as identity,
    url text not null,
    last_updated timestamp with time zone not null,
    primary key (id)
);