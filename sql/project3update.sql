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
confNo char(8) primary key,
vtname varchar(9),
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
odometer varchar(9),
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
odometer varchar(9),
status varchar(20),
vtname varchar(9),
location varchar(20),
city varchar(20))
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
rid char(8) primary key,
returnDateTime timestamp,
odometer varchar(9),
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
