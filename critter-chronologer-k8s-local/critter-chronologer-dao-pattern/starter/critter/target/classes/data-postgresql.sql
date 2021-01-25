INSERT INTO EMPLOYEES (id, name) VALUES
(1, 'James Mary'),
(2, 'John Patricia'),
(3, 'Robert Jennifer'),
(4, 'Michael Linda'),
(5, 'William Elizabeth'),
(6, 'Barbara David'),
(7, 'Susan Richard'),
(8, 'Jessica Joseph'),
(9, 'Sarah Thomas'),
(10, 'Karen Charles');

INSERT INTO EMPLOYEE_SKILLS (employeeid, skill) VALUES
(1, 'MEDICATING'),
(1, 'SHAVING'),
(1, 'PETTING'),
(2, 'FEEDING'),
(2, 'WALKING'),
(2, 'PETTING'),
(3, 'FEEDING'),
(3, 'WALKING'),
(3, 'PETTING'),
(4, 'FEEDING'),
(4, 'WALKING'),
(4, 'PETTING'),
(4, 'SHAVING'),
(5, 'WALKING'),
(5, 'PETTING'),
(5, 'SHAVING'),
(5, 'MEDICATING'),
(5, 'FEEDING'),
(6, 'FEEDING'),
(6, 'WALKING'),
(6, 'PETTING'),
(7, 'FEEDING'),
(7, 'WALKING'),
(7, 'PETTING'),
(8, 'FEEDING'),
(8, 'WALKING'),
(8, 'PETTING'),
(9, 'FEEDING'),
(9, 'WALKING'),
(10, 'PETTING');

INSERT INTO EMPLOYEE_AVAILABILITIES (employeeid, dayavailable) VALUES
(1, 'MONDAY'),
(1, 'WEDNESDAY'),
(1, 'FRIDAY'),
(2, 'MONDAY'),
(2, 'TUESDAY'),
(2, 'WEDNESDAY'),
(2, 'THURSDAY'),
(2, 'FRIDAY'),
(2, 'SATURDAY'),
(2, 'SUNDAY'),
(3, 'MONDAY'),
(3, 'TUESDAY'),
(3, 'WEDNESDAY'),
(3, 'THURSDAY'),
(3, 'FRIDAY'),
(3, 'SATURDAY'),
(3, 'SUNDAY'),
(4, 'MONDAY'),
(4, 'TUESDAY'),
(5, 'WEDNESDAY'),
(5, 'THURSDAY'),
(6, 'FRIDAY'),
(6, 'SATURDAY'),
(6, 'SUNDAY'),
(6, 'MONDAY'),
(7, 'TUESDAY'),
(7, 'WEDNESDAY'),
(7, 'THURSDAY'),
(8, 'FRIDAY'),
(8, 'SATURDAY'),
(8, 'SUNDAY'),
(9, 'MONDAY'),
(9, 'TUESDAY'),
(9, 'WEDNESDAY'),
(10, 'THURSDAY'),
(10, 'FRIDAY'),
(10, 'SATURDAY'),
(10, 'SUNDAY');

INSERT INTO customers (id, name, phonenumber, notes) VALUES
(1, 'Nancy Christopher', '332-554-3768', 'customer 1'),
(2, 'Lisa Daniel', '332-524-3668', 'customer 2'),
(3, 'Margaret Matthew', '339-999-3768', 'customer 3'),
(4, 'Anthony Betty', '332-0021-3768', 'customer 4'),
(5, 'Donald Sandra', '991-554-3768', 'customer 5');

INSERT INTO pets (id, name, type, ownerid, birthdate, notes) VALUES
(1, 'Bella' , 'BIRD', 1, '2012-07-06', 'pet 1'),
(2, 'Charlie', 'DOG', 2, '2013-08-11', 'pet 2'),
(3, 'Luna', 'DOG', 2, '2013-08-11', 'pet 3'),
(4, 'Lucy', 'LIZARD', 3, '2015-08-11', 'pet 4'),
(5, 'Max', 'DOG', 4, '2017-02-11', 'pet 5'),
(6, 'Bailey', 'DOG', 4, '2018-01-11', 'pet 6'),
(7, 'Cooper', 'BIRD', 5, '2013-08-11', 'pet 7'),
(8, 'Daisy', 'CAT', 5, '2013-08-11', 'pet 8');

INSERT INTO appointments(id, petid, employeeid, activity, serviceday, servicetime) VALUES
(1, 1, 1, 'MEDICATING', 'MONDAY', '8:00AM'),
(2, 2, 2, 'MEDICATING', 'MONDAY', '8:00AM');







