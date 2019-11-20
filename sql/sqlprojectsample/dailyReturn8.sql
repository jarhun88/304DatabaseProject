SELECT sum(r.value), count(*)
FROM Return r, Rent rt, Vehicle v
WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI') AND r.returnDateTime <= 
to_timestamp('2019-01-07:23:59', 'YYYY-MM-DD:HH24:MI') AND v.location = 'UBC' AND v.city = 'Vancouver'
/
