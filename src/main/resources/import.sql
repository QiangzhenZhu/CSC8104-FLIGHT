-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-1');
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-2');
-- insert into myentity (id, field) values(nextval('hibernate_sequence'), 'field-3');

insert into Flight (id, flightNumber, departure, destination) values (22222, 'af123', 'NCL', 'CDN');
insert into Flight (id, flightNumber, departure, destination) values (22223, 'jk123', 'NJF', 'QAT');
insert into Flight (id, flightNumber, departure, destination) values (22224, 'NK777', 'JKL', 'ASD');
insert into Customer(id, `name`, email, phoneNumber) values (11111, 'sun', 'sun@newcastle.uk', '01234567890');
insert into Customer(id, `name`, email, phoneNumber) values (11112, 'john', 'john@newcastle.uk', '01234567891');
insert into Customer(id, `name`, email, phoneNumber) values (11113, 'mary', 'mary@newcastle.uk', '01234567892');

-- 调用测试数据，后期注释掉
-- insert into Customer(id, `name`, email, phoneNumber) values (1, 'Davinci', 'bestArtist@google.com', '01357924680');
