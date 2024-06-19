create table public.mail
(
    id      serial not null,
    user_id integer unique,
    primary key (id)
);

alter table if exists public.mail
    add constraint mail_user_fk
    foreign key (user_id) references public.users;