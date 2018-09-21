insert into app_user(id,name,username,password) values (1,'Kuba','admin','$2a$11$91yZ18mkqjbswmV.VNgVgu6k5Cczg6Bw4CIaaYNVGgF5c.ElOabSa');
insert into app_user(id,name,username,password) values (2,'Krystyna','user','$2a$11$DAWuze8ei2xOkBQMM5226OhNKRN6URlGBPudLh0uRLdYv6i1zWK/W');

INSERT INTO AUTHORITY (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO AUTHORITY (id, name) VALUES (2, 'ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (1, 1);
INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (1, 2);
INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (2, 1);