delete from public.user_channel_preferences;
delete from public.channel_roles;
delete from public.user_roles;
delete from public.users;
delete from public.channel;
delete from public.notification;


INSERT INTO public.channel (id, name, creation_date, last_update_date) VALUES (1, 'test-admin-channel', now(), now());
INSERT INTO public.channel (id, name, creation_date, last_update_date) VALUES (2, 'test-user-channel', now(), now());

INSERT INTO public.channel_roles (channel_id, roles) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.channel_roles (channel_id, roles) VALUES (2, 'ROLE_USER');







INSERT INTO public.users (id, email, creation_date, last_update_date) VALUES (1, 'admin@mail.com', now(), now());
INSERT INTO public.users (id, email, creation_date, last_update_date) VALUES (2, 'user@mail.com', now(), now());

INSERT INTO public.user_roles (user_id, roles) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.user_roles (user_id, roles) VALUES (2, 'ROLE_USER');
