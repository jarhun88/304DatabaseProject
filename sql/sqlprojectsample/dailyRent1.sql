SELECT * 
FROM Rent r, Vehicle v
WHERE fromDateTime <= to_timestamp('2019-01-07:00:00', 'YYYY-MM-DD:HH24:MI') AND toDateTime >=  to_timestamp('2019-01-07:23:59', 'YYYY-MM-DD:HH24:MI')  
AND r.vid = v.vid
ORDER BY v.city, v.location, v.vtname
/

