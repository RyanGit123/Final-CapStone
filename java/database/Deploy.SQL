BEGIN TRANSACTION;
-- drop all tables***************************
DROP TABLE IF EXISTS users, properties,maintenance_requests,rental_agreements, payment_history;

-- create all tables***************************
CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	email varchar(50) not null UNIQUE,
	name varchar (50) not null,
	property_id INTEGER DEFAULT 0 not null,
	apartment_id INTEGER DEFAULT 0 not null,
  role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)

);

  Create Table properties(
  property_id SERIAL,
  address_street varchar(50) not null,
  address_city varchar(50) not null,
  address_state varchar(2) not null,
  address_zip varchar(10) not null,
  description text not null,
  status boolean not null,
  bathroom_count DOUBLE PRECISION,
  room_count INTEGER,
  price DOUBLE PRECISION








  );
  Create Table maintenance_requests(
  id SERIAL primary key,
  property_id integer not null,
  description text not null,
  completed boolean not null default false,
  request_date date not null default current_date,
  due_date date,
  completion_date date,
  assigned_worker_id integer default 0 not null



  );





    Create TABLE rental_agreements(
    agreement_id SERIAL,
    renter_id int not null,
    landlord_id int not null,
    property_id int not null,
    lease_start_date timeStamp not null,
    lease_end_date timeStamp not null,
    rent_amount int not null,
    CONSTRAINT PK_agreement_id PRIMARY KEY (agreement_id),
    CONSTRAINT FK_renter_id_users_user_id FOREIGN KEY (renter_id) REFERENCES users(user_id)





    );









  Create TABLE payment_history(
  payment_id SERIAL primary key,
  user_id int not null,
  property_id int not null,
  amount_paid int not null,
  amount_due int not null,
  renters_name varchar(50) not null,
  late_fee int default 0,
  payment_method varchar(50) not null,
  payment_date DATE,
  due_date DATE
  );









-- populate all tables***************************
INSERT INTO users (username,password_hash,role,name,email) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER','john wells','johnwells1@test.com');
INSERT INTO users (username,password_hash,role,name,email) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN','john wells','johnwells@test.com');

INSERT INTO public.properties(
	 address_street, address_city, address_state, address_zip, description, status, bathroom_count, room_count, price)
	VALUES ( '1313 mockingbird ln', 'wilmington','DE', '19804','nice place to rent' , true, 4, 4, 150000.00);

INSERT INTO public.maintenance_requests(
description, completed, request_date, due_date, completion_date, assigned_worker_id, property_id)
VALUES ('need my sink fixed',false,'2024-08-12','2024-08-13','2024-08-13',1,2);

INSERT INTO public.rental_agreements(
renter_id,landlord_id,property_id,lease_start_date,lease_end_date,rent_amount)
VALUES (1,2,2,'2024-01-01','2025-01-01',1200);

INSERT INTO public.payment_history(
payment_id, user_id, property_id, amount_paid, amount_due, renters_name, late_fee, payment_method, payment_date, due_date)
VALUES (1,1,2,1200,1250,'billy bob',150,'credit card', '2024-09-15','2024-09-20');



GRANT ALL
ON ALL TABLES IN SCHEMA public
TO final_capstone_owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO final_capstone_owner;




GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO final_capstone_appuser;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO final_capstone_appuser;




COMMIT TRANSACTION;
