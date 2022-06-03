--a) Devolver clientes que no se registraron en habitaciones dobles
SELECT r.dni_cliente, h.cant_camas FROM hotel.registros as r JOIN hotel.habitaciones as h ON(r.nro_hab = h.nro) where h.cant_camas <> 'doble';

--b)Listar los clientes con el total que abonó en todas sus registraciones.
SELECT dni_cliente, SUM(costo) FROM hotel.registros GROUP BY dni_cliente;

--c)Listar el personal que es cliente del Hotel con todos sus datos personales.
SELECT dni, nyap, telefono, direccion FROM hotel.personas NATURAL JOIN hotel.personal NATURAL JOIN hotel.clientes;

--d) Definir consultas propias (no menos de tres), donde por lo menos una utilice subconsultas.

--Listar datos personales de clientes que no forman parte del personal.
select dni, nyap, telefono, direccion from clientes natural join personas where dni not in (select dni from personal);

--Listar dni de aquellos clientes que se hospedaron mas de una vez
select dni_cliente, count(*) as cantidad_hospedajes from registros group by dni_cliente having count(*) > 1;

--Listar las habitaciones y la mucama que la atiende
select nro, dni, nyap from habitaciones as h join atiende as a on(h.nro = a.nro_hab) join personas as p on(p.dni = a.dni_mucama);