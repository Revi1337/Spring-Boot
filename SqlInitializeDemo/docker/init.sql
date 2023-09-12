drop table if exists member;
create table member (
    id bigint not null auto_increment,
    name varchar(255),
    password varchar(255),
    primary key (id)
) engine=InnoDB