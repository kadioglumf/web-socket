INSERT INTO public.channel (id, name) VALUES (1, 'test-admin-channel');
INSERT INTO public.channel (id, name) VALUES (2, 'test-user-channel');


INSERT INTO public.channel_roles (channel_id, roles) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.channel_roles (channel_id, roles) VALUES (2, 'ROLE_USER');
