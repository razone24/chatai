drop table if exists wiki;
create table if not exists wiki
(
    id              serial primary key,
    feature         text not null,
    owner           text not null,
    description     text not null
);