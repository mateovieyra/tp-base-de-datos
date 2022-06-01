drop schema if exists hotel cascade;
CREATE schema hotel;

SET search_path TO hotel;


CREATE DOMAIN tipo_cama AS varchar(10) 
CHECK (value IN ('simple', 'doble', 'triple', 'cuadruple'));
 

DROP TABLE IF EXISTS  personas;
CREATE TABLE  personas (
  dni varchar(10) not null, 
  nyap varchar(50) not null, 
  telefono varchar(20) not null,
  direccion varchar(50) not null,
  constraint pk_dni_persona primary key(dni)
);

DROP TABLE IF EXISTS  personal;
CREATE TABLE  personal (
  dni varchar(10) not null,
  antiguedad integer not null, 
  sueldo float not null,
  constraint pk_dni_personal primary key(dni),
  constraint fk_personal_personas foreign key(dni) references personas on delete cascade on update cascade,
  constraint chk_antiguedad check(antiguedad >= 0),
  constraint chk_sueldo check(sueldo >= 0)
);

DROP TABLE IF EXISTS  clientes;
CREATE TABLE  clientes (
  dni varchar(10) not null,
  fecha_primer_hosp date not null,
  constraint pk_dni_cliente primary key(dni),
  constraint fk_clientes_personas foreign key(dni) references personas on delete cascade on update cascade
);

DROP TABLE IF EXISTS  gerentes;
CREATE TABLE  gerentes (
  dni varchar(10) not null,
  comision float not null,
  constraint pk_dni_gerente primary key(dni),
  constraint fk_gerentes_personas foreign key(dni) references personas on delete cascade on update cascade
);  

DROP TABLE IF EXISTS mucamas;
CREATE TABLE mucamas(
dni varchar(10) NOT NULL,
constraint pk_dni_mucama PRIMARY KEY (dni),
constraint fk_mucamas_personal foreign key (dni) references personal(dni) on delete cascade on update cascade
);

DROP TABLE IF EXISTS tipo_habitaciones;
CREATE TABLE tipo_habitaciones(
codigo serial NOT NULL,
descripcion varchar(40) default '',
costo float NOT NULL,
constraint pk_codigo PRIMARY KEY (codigo),
constraint chk_costo check (costo >= 0)
);

DROP TABLE IF EXISTS habitaciones;
CREATE TABLE habitaciones(
nro integer NOT NULL,
cant_camas tipo_cama NOT NULL,
codigo_tipo_hab integer,
CONSTRAINT pk_habitaciones PRIMARY KEY (nro),
CONSTRAINT fk_habitaciones_tipo_habitaciones FOREIGN KEY (codigo_tipo_hab) REFERENCES tipo_habitaciones(codigo) on update cascade on delete set null
);

DROP TABLE IF EXISTS  atiende;
CREATE TABLE  atiende (
  dni_mucama varchar(10) not null,
  nro_hab integer not null, 
  constraint pk_dni_nro_hab primary key(dni_mucama, nro_hab),
  constraint fk_atiende_mucamas foreign key(dni_mucama) references mucamas(dni) on delete cascade on update cascade,
  constraint fk_atiende_habitaciones foreign key(nro_hab) references habitaciones(nro) on delete cascade on update cascade
);  

DROP TABLE IF EXISTS registros;
CREATE TABLE registros(
fecha date NOT NULL,
nro_hab integer,
dni_cliente varchar(10) NOT NULL,
cant_dias integer NOT NULL,
costo float NOT NULL,
constraint pk_fecha_nro_hab primary key(fecha,nro_hab),
constraint fk_registros_clientes foreign key (dni_cliente) references clientes (dni) on delete cascade on update cascade,
constraint fk_resgistros_habitaciones foreign key (nro_hab) references habitaciones (nro) on delete set null on update cascade,
constraint unique_fecha_cliente unique(dni_cliente, fecha),
constraint chk_cant_dias check (cant_dias >= 0),
constraint chk_costo check (costo >= 0)
);

DROP TABLE IF EXISTS auditoria_clientes;
CREATE TABLE auditoria_clientes(
nro_hab integer not null,
fecha_cambio date not null,
dni_cliente_anterior varchar(10),
dni_cliente_nuevo varchar(10),
usuario_cambio name not null, 
constraint pk_auditoria primary key(nro_hab, fecha_cambio, dni_cliente_anterior, dni_cliente_nuevo),
constraint fk_auditoria_cliente_anterior foreign key(dni_cliente_anterior) references clientes(dni) on update cascade on delete set null,
constraint fk_auditoria_cliente_nuevo foreign key(dni_cliente_nuevo) references clientes(dni) on update cascade on delete set null
);

CREATE FUNCTION auditoria() RETURNS trigger
AS $$
begin
  insert into auditoria_clientes values(old.nro_hab, now(), old.dni_cliente, new.dni_cliente, current_user);
  return new;
END
$$
LANGUAGE plpgsql;

create trigger trigger_auditoria_clientes after update on registros for each row execute procedure auditoria();
