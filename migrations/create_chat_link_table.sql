--liquibase formatted sql

create table if not exists chat_links (
    chat_id bigint not null references chat (id) on delete cascade,
    link_id bigint not null references link (id) on delete cascade,
    primary key (chat_id, link_id)
);
