SET search_path TO hotel;

INSERT INTO personas 
values ('42.638.323','Mateo Vieyra', '3584235689', 'Maipu 456'),
('32.458.323','Juan Perez', '3584235123', 'San Martin 456'),
('23.698.147', 'German Gonzalez', '3585357412', 'Belisario Roldan 98'),
('17.357.159', 'Pedro Perez', '3584156287', 'Mitre 236'),
('25.321.159', 'Mariana Lopez', '3585147963', 'Mendoza 334'),
('43.810.323','Maria Perez', '3584235423', 'Alberdi 36'),
('43.890.323','Brenda Andrada', '3584295423', 'Guemes 456'),
('43.555.323','Lulio Mansilla', '3584210423', 'San lorenzo 456'),
('23.110.050','Ezequiel Fernandez', '3584910257','San Martin 900'),
('10.910.020', 'Laida Compagnnucci', '3584105492','Suipacha 355');

INSERT INTO tipo_habitaciones(descripcion, costo) values
('Basica', '7000'),
('Intermedia', '15000'),
('Premium', '30000');

INSERT INTO habitaciones(nro,cant_camas,codigo_tipo_hab) values
(1, 'simple', 1),
(2, 'simple', 2),
(3, 'simple', 3),
(4, 'doble', 1),
(5, 'doble', 2),
(6, 'doble', 3),
(7, 'triple', 1),
(8, 'triple', 2),
(9, 'triple', 3),
(10, 'cuadruple', 1),
(11, 'cuadruple', 2),
(12, 'cuadruple', 3);


INSERT INTO clientes values
('42.638.323', '2022-01-01'),
('32.458.323', '2022-01-10'),
('43.890.323', '2022-02-15'),
('43.555.323', '2022-03-30');

INSERT INTO personal values
('17.357.159', 10, 90000),
('25.321.159', 3,  45000),
('43.890.323', 2,  90000),
('43.810.323', 5,  45000),
('10.910.020', 20, 55000);

INSERT INTO gerentes values 
('17.357.159',8000),
('43.890.323',3000);

INSERT INTO mucamas values
('25.321.159'),
('43.810.323'),
('10.910.020');


INSERT INTO atiende values
('25.321.159', 1),
('25.321.159', 2),
('25.321.159', 3),
('25.321.159', 4),
('43.810.323', 5),
('43.810.323', 6),
('43.810.323', 7),
('43.810.323', 8),
('10.910.020', 1),
('10.910.020', 2),
('10.910.020', 9),
('10.910.020', 10),
('10.910.020', 11),
('10.910.020', 12);

INSERT INTO registros values
('2022-01-01', 1, '42.638.323', 2, 14000),
('2022-01-10', 2, '32.458.323', 5, 60000),
('2022-02-15', 2, '43.890.323', 10, 70000),
('2022-03-30', 9, '43.555.323', 1, 30000);

