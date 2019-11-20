delete from reservation where cellphone = '1111111111'
/
insert into reservation (vid, cellphone, fromDateTime, toDateTime) values (
6, '1111111111', to_timestamp('2019-03-03:00:00', 'YYYY-MM-DD:HH24:MI'),
to_timestamp('2019-03-19:00:00', 'YYYY-MM-DD:HH24:MI'))
/

