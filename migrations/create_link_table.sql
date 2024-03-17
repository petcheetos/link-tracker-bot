--liquibase formatted sql

create table if not exists link (
    id bigint generated always as identity,
    url text not null,
    last_updated timestamp with time zone not null default current_timestamp,
    checked_at timestamp with time zone not null default timestamp 'epoch',
    primary key (id)
);
