--liquibase formatted sql

create table if not exists chat(
	id bigint not null,
	created_at timestamp with time zone not null,
	primary key(id)
);
