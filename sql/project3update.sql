drop table Reservation
/
drop table Rent
/
drop table Vehicle
/
drop table VehicleType
/
drop table Customer
/
drop table Return
/
drop table Reservation
/
create table Reservation(
confNo char(8) primary key,
vtname varchar(8),
cellphone char(10),
fromDateTime date,
toDateTime date)
/
create table Rent(
rid char(8) primary key,
vid char(8),
cellphone char(10),
fromDateTime date,
toDateTime date,
odometer varchar(6),
cardName varchar(50),
cardNo char(16),
expDate date,
confNo char(8))
/
create table Vehicle(
vid char(8) primary key,
vlicense char(6),
make varchar(30),
model varchar(30),
year varchar(4),
color varchar(20),
odometer varchar(6),
status varchar(20),
vtname varchar(8),
location varchar(20),
city varchar(20))
/
create table VehicleType(
vtname varchar(8) primary key,
features varchar(100),
wrate integer,
drate integer,
hrate integer,
wirate integer,
dirate integer,
hirate integer,
krate integer)
/
create table Customer(
cellphone char(10) primary key,
name varchar(50),
address varchar(50),
dlicense varchar(9))
/
create table Return(
rid char(8) primary key,
returnDateTime date,
odometer varchar(6),
fulltank char(1),
varlue integer)
/
alter table Reservation
add foreign key(vtname)
references VehicleType(vtname)
/
alter table
Rent
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
