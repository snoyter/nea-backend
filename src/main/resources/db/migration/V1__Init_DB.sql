create table public.dump
(
    id         serial                      not null,
    latitude   float(53)                   not null,
    longitude  float(53)                   not null,
    user_id    integer,
    created_at timestamp(6) with time zone not null default now(),
    comment    text                        not null,
    primary key (id)
);

create table public.dump_to_file
(
    dump_id integer not null,
    file_id integer not null,
    primary key (dump_id, file_id)
);

create table public.file
(
    id         serial                      not null,
    created_at timestamp(6) with time zone not null default now(),
    content    varchar(255)                not null,
    name       varchar(255)                not null,
    type       varchar(255)                not null,
    primary key (id)
);

create table public.publication
(
    id         serial                      not null,
    type       integer                     not null,
    created_at timestamp(6) with time zone not null default now(),
    content    TEXT                        not null,
    title      varchar(255)                not null,
    primary key (id)
);

create table public.publication_type
(
    id   serial       not null,
    type varchar(255) not null,
    primary key (id)
);

create table public.user_type
(
    id   serial       not null,
    type varchar(255) not null,
    primary key (id)
);

create table public.users
(
    id         serial                      not null,
    type       integer                     not null,
    created_at timestamp(6) with time zone not null default now(),
    login      varchar(255)                not null unique,
    password   varchar(255)                not null,
    primary key (id)
);

alter table if exists public.dump
    add constraint dump_user_fk
    foreign key (user_id) references public.users;

alter table if exists public.dump_to_file
    add constraint dump_file_fk
    foreign key (file_id) references public.file;

alter table if exists public.dump_to_file
    add constraint file_dump_fk
    foreign key (dump_id) references public.dump;

alter table if exists public.publication
    add constraint publication_to_publication_type_fk
    foreign key (type) references public.publication_type;

alter table if exists public.usr
    add constraint user_to_user_type_fk
    foreign key (type) references public.user_type;