SELECT COUNT(*)
FROM Rent
WHERE fromDateTime >=  to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI') AND fromDateTime <=  to_timestamp('2019-01-07:23:59','YYYY-MM-DD:HH24:MI')
/
