--liquibase formatted sql

create table if not exists chat(
	id bigint not null,
	primary key(id)
);
