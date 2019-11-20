delete from return where rid = 13
/
insert into return values (13, to_date('2200-03-19','YYYY-MM-DD'), '999999999', 'T', 1000)
/
update vehicle set status = 'available' where vid = ANY (select v.vid from vehicle v, rent r where v.vid = r.vid and r.rid = 13)
/
 
