create table public.request
(
    id         serial                      not null,
    user_id    integer,
    approved   boolean default false,
    created_at timestamp(6) with time zone not null default now(),
    primary key (id)
);

alter table if exists public.request
    add constraint request_user_fk
        foreign key (user_id) references public.users;
