create table if not exists Person (
	id bigint auto_increment primary key,
	version int null,
	createdBy varchar(255) null,
	createdDateTime datetime(6) not null,
	lastModifiedBy varchar(255) null,
	lastModifiedDateTime datetime(6) not null,
	birthDateTime datetime(6) null,
	firstName varchar(255) not null,
	lastName varchar(255) not null,
	address_id bigint null,
	constraint FK6i7nduc8blbwp1dbfwavvnvvx
		foreign key (address_id) references Address (id)
);
