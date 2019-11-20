delete from rent where confNo = 14
/
insert into rent (vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo) values (
4, '9099091010', to_timestamp('2200-01-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2200-02-02:00:00','YYYY-MM-DD:HH24:MI'),
999999999, 'Visa', '4444555566667777', to_date('2200-07-01', 'YYYY-MM-DD'), 14)
/
update vehicle set status = 'rented' where vid = 4
/

