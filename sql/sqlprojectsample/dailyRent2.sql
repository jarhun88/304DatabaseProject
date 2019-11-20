SELECT COUNT(*), v.vtname
FROM Vehicle v, Rent r
WHERE fromDateTime <= to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI') AND toDateTime >= to_timestamp('2019-01-07:23:59','YYYY-MM-DD:HH24:MI') AND r.vid = 
v.vid
GROUP BY v.vtname
/
