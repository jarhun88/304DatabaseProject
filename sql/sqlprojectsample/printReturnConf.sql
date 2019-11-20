select rt.rid, r.confNo, rt.returnDateTime, rt.value
from return rt, rent r
where rt.rid = r.rid and rt.rid = 13
/
