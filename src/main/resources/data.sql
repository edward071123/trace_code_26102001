create database if not exists inventorydb;
use inventorydb;

create table if not exists products (
    id integer not null,
    name varchar(255),
    primary key (id)
);

create table if not exists stocks (
    id bigint not null,
    warehouse varchar(255),
    product_id integer not null,
    quantity integer not null,
    primary key (id),
    constraint fk_stocks_product
        foreign key (product_id) references products (id)
);

insert into products (id, name) values (101, 'Keyboard');
insert into products (id, name) values (102, 'Mouse');
insert into products (id, name) values (103, 'Webcam');

insert into stocks (id, warehouse, product_id, quantity) values (1, 'Taipei', 101, 8);
insert into stocks (id, warehouse, product_id, quantity) values (2, 'Taichung', 101, 3);
insert into stocks (id, warehouse, product_id, quantity) values (3, 'Taipei', 102, 5);
insert into stocks (id, warehouse, product_id, quantity) values (4, 'Taichung', 102, 6);
insert into stocks (id, warehouse, product_id, quantity) values (5, 'Taipei', 103, 4);
insert into stocks (id, warehouse, product_id, quantity) values (6, 'Taichung', 103, 20);

