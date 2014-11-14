



ALTER TABLE it_document ADD INDEX docu_views ( `views` );
ALTER TABLE it_document ADD INDEX docu_remarks ( `remarks` );
ALTER TABLE it_document ADD INDEX docu_createDate ( `createDate` );
ALTER TABLE it_document ADD INDEX docu_catalogId ( `catalogId` );
ALTER TABLE it_document ADD INDEX docu_id ( `id` );
ALTER TABLE it_document ADD INDEX docu_userid ( `userid` );

alter table it_document add sameId bigint(20) default 0; 
alter table it_document add canRun tinyint(1) default 0; 

alter table it_document add mark tinyint(4) default 0; 
alter table it_document add downVotes bigint(4) default 0; 