select count(*) from vehicle v where v.vtname = 'Compact' and v.location = 'UBC' and v.city = 'Vancouver'
and v.vid not in (select r.vid from reservation r, vehicle v1 where v1.vid = r.vid and r.fromDateTime <= to_timestamp('2019-01-02:00:00','YYYY-MM-DD:HH24:MI')
and r.toDateTime >= to_timestamp('2019-01-03','YYYY-MM-DD'))
/
