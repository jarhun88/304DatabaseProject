select * from VehicleType vt
where vt.vtname = ANY( select v.vtname From rent r, vehicle v where r.rid = 3 and r.vid = v.vid)
/

