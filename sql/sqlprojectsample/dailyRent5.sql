SELECT * 
FROM Rent r, Vehicle v
WHERE fromDateTime <=  to_timestamp('2019-01-01:00:00','YYYY-MM-DD:HH24:MI') AND toDateTime >=  to_timestamp('2019-01-01:23:59', 'YYYY-MM-DD:HH24:MI')  
AND r.vid = v.vid AND v.location = 'UBC' AND v.city = 'Vancouver'
ORDER BY v.vtname
/

