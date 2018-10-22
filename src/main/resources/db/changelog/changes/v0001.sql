create table if not exists Address (
	id bigint auto_increment primary key,
	version int null,
	createdBy varchar(255) null,
	createdDateTime datetime(6) not null,
	lastModifiedBy varchar(255) null,
	lastModifiedDateTime datetime(6) not null,
	city varchar(255) null,
	country varchar(255) null,
	no varchar(255) null,
	state varchar(255) null,
	street varchar(255) null,
	zipCode varchar(255) null
);
