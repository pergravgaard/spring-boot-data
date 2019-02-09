create table if not exists Car (
	id bigint auto_increment primary key,
	version int null,
	createdBy varchar(255) null,
	createdDateTime datetime(6) not null,
	lastModifiedBy varchar(255) null,
	lastModifiedDateTime datetime(6) not null,
	model varchar(255) not null
);
