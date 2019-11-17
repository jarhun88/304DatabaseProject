drop table Reservation cascade constraints purge
/
drop table Rent cascade constraints purge
/
drop table Vehicle cascade constraints purge
/
drop table VehicleType cascade constraints purge
/
drop table Customer cascade constraints purge
/
drop table Return cascade constraints purge
/
drop table Branch cascade constraints purge
/
create table Reservation(
confNo integer generated always as identity,
vtname varchar(9),
cellphone char(10),
fromDateTime timestamp,
toDateTime timestamp,
primary key(confNo))
/
create table Rent(
rid integer generated always as identity,
vid integer,
cellphone char(10),
fromDateTime date,
toDateTime date,
odometer varchar(9),
cardName varchar(50),
cardNo char(16),
expDate date,
confNo integer,
primary key (rid))
/
create table Vehicle(
vid integer generated always as identity,
vlicense char(6),
make varchar(30),
model varchar(30),
year varchar(4),
color varchar(20),
odometer varchar(9),
status varchar(20),
vtname varchar(9),
location varchar(20),
city varchar(20),
primary key(vid))
/
create table VehicleType(
vtname varchar(9) primary key,
features varchar(100),
wrate float,
drate float,
hrate float,
wirate float,
dirate float,
hirate float,
krate float)
/
create table Customer(
cellphone char(10) primary key,
name varchar(50),
address varchar(50),
dlicense char(9))
/
create table Return(
rid integer primary key,
returnDateTime timestamp,
odometer varchar(9),
fulltank char(1),
value integer)
/
alter table Reservation
add foreign key(vtname)
references VehicleType(vtname)
/
alter table Reservation
add foreign key(cellphone)
references Customer(cellphone)
/
alter table Rent
add foreign key (vid)
references Vehicle (vid)
/
alter table Rent
add foreign key (cellphone)
references Customer (cellphone)
/
alter table Rent
add foreign key (confNo)
references Reservation (confNo)
/
alter table Vehicle
add foreign key (vtname)
references VehicleType (vtname)
/
create table Branch(
location varchar(20),
city varchar(20),
primary key (location, city))
/
alter table Vehicle
add foreign key (location, city)
references Branch (location, city)
/
alter table Return
add foreign key (rid)
references Rent (rid)
/
insert into branch Values (
'UBC', 'Vancouver')
/
insert into branch values (
'Downtown', 'Vancouver')
/
insert into branch values (
'East', 'Richmond')
/
insert into Customer values (
'6041234567', 'John Song', '2366 Main Mall, Vancouver, BC', '789456123')
/
insert into Customer values (
'6041237890', 'Satori Kitamori', '5726 University Blvd, Vancouver, BC', '789456124')
/
insert into Customer values (
'7804564567', 'James Ens', '6133 University Blvd, Vancouver, BC', '789456125')
/
insert into Customer values (
'7804561234', 'Jon Snow', '1 Winterfell Rd, Westeros', '789456126')
/
insert into Customer values (
'6041231234', 'Darth Vader', '4 Tusken blvd, Tatooine', '789456127')
/
insert into Customer values (
'1234567890', 'Frodo Baggins', '8 Hobbit St, Shire', '789456128')
/
insert into Customer values (
'1234564560', 'Freddy Krueger', '5 Elm Street, Spooky Town', '789456129')
/
insert into Customer values (
'1237894567', 'Jessica Wong', '2366 Main Mall, Vancouver, BC', '789456130')
/
insert into Customer values (
'4561237890', 'Raymond Ng', '2366 Main Mall, Vancouver, BC', '789456131')
/
insert into Customer values (
'4564561234', 'Santa Ono', '123 Sesame st, Vancouver, BC', '789456132')
/
insert into VehicleType values (
'Economy', null, 60, 10, 0.5, 10, 5, 1, 0.25)
/
insert into VehicleType values (
'Compact', null, 60, 10, 0.5, 10, 5, 1, 0.25)
/
insert into VehicleType values (
'Mid-size', null, 100, 15, 0.75, 5, 2, 1, 0.5)
/
insert into VehicleType values (
'Standard', null, 60, 10, 0.5, 10 ,5, 1, 0.25)
/
insert into VehicleType values (
'Full-size', null, 130, 20, 1.5, 20, 10, 5, 0.5)
/
insert into VehicleType values (
'SUV', null, 130, 20, 1.5, 20, 10, 5, 0.5)
/
insert into VehicleType values (
'Truck', null, 130, 20, 1.5, 20, 10 , 5, 0.5)
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123456', 'Ford', 'Fusion', '2019', 'Red', '123456789', 'available',
'Economy', 'UBC', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123457', 'Ford', 'EcoSport', '2019', 'Black', '200000008', 'available',
'Compact', 'UBC', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123458', 'Ford', 'Ranger', '2019', 'Green', '321221458', 'available',
'Mid-size', 'UBC', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123459', 'Ford', 'Fusion', '2018', 'Red Black', '151031724', 'available',
'Standard', 'UBC', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123460', 'GMC', 'Yukon', '2018', 'Grey', '000990128', 'maintenance',
'Full-size', 'Downtown', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123461', 'GMC', 'Acadia', '2018', 'Grey', '000000000', 'available',
'SUV', 'Downtown', 'Vancouver')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123462', 'Ford', 'F-150', '2018', 'Black', '321221458', 'available',
'Truck', 'East', 'Richmond')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123463', 'Ford', 'F-150', '2012', 'Red', '321221458', 'available',
'Truck', 'East', 'Richmond')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123464', 'Honda', 'CR-V', '2012', 'Grey', '321221458', 'available',
'SUV', 'East', 'Richmond')
/
insert into vehicle (vlicense, make, model, year, color, odometer, status, vtname, location, city) values (
'123465', 'Honda', 'Accord', '2012', 'Black', '321221458', 'available',
'Economy', 'East', 'Richmond')
/
insert into reservation (vtname, cellphone, fromDateTime, toDateTime) values (
'Economy', '6041234567', to_timestamp('2019-01-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vtname, cellphone, fromDateTime, toDateTime) values (
'Compact', '6041237890', to_timestamp('2019-01-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vtname, cellphone, fromDateTime, toDateTime) values (
'Mid-size', '7804564567', to_timestamp('2019-06-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-06-30:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vtname, cellphone, fromDateTime, toDateTime) values (
'Standard', '7804561234', to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-02:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vtname, cellphone, fromDateTime, toDateTime) values (
'Full-size', '6041231234', to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-02:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vtname, cellphone, fromDateTime, toDateTime) values (
'SUV', '1234567890', to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-03-01:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vtname, cellphone, fromDateTime, toDateTime) values (
'Truck', '1234564560', to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-03-01:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vtname, cellphone, fromDateTime, toDateTime) values (
'Truck', '1237894567', to_timestamp('2019-01-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vtname, cellphone, fromDateTime, toDateTime) values (
'SUV', '4561237890', to_timestamp('2019-01-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'))
/
insert into reservation (vtname, cellphone, fromDateTime, toDateTime) values (
'Economy', '4564561234', to_timestamp('2019-01-01:00:00','YYYY-MM-DD:HH24:MI'), to_timestamp('2019-02-01:00:00','YYYY-MM-DD:HH24:MI'))
/

