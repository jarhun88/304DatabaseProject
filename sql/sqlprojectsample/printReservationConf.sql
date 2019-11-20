select * from reservation where confNo =
(select max(confNo) from reservation)
/

