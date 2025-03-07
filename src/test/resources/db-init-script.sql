create sequence file_id_seq;
create table file
(
    id       bigint primary key not null default nextval('file_id_seq'),
    capacity integer            not null,
    name     varchar(50)        not null
);
create sequence tag_id_seq;
create table tag
(
    id   bigint primary key not null default nextval('tag_id_seq'),
    name varchar(50)        not null
);
create table file_tag
(
    file_id BIGINT REFERENCES file (id) NOT NULL,
    tag_id  BIGINT REFERENCES tag (id)  NOT NULL,
    PRIMARY KEY (file_id, tag_id)
);