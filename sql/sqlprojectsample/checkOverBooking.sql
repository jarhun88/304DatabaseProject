select count(*)
from reservation
where vid = 6 and fromDateTime <= to_timestamp('2019-02-02:00:00', 'YYYY-MM-DD:HH24:MI')
and toDateTime >= to_timestamp('2019-02-19:00:00', 'YYYY-MM-DD:HH24:MI')
/
select count(*)
from reservation
where vid = 6 and fromDateTime <= to_timestamp('2019-03-02:00:00', 'YYYY-MM-DD:HH24:MI')
and toDateTime >= to_timestamp('2019-03-19:00:00', 'YYYY-MM-DD:HH24:MI')
/
