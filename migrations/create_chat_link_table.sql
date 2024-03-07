--liquibase formatted sql
create table if not exists chat_links (
	id_chat bigint not null references chat (id) on delete cascade,
	id_link bigint not null references link (id) on delete cascade,
	primary key (id_chat, id_link)
);