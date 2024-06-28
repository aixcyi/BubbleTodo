CREATE TABLE bubble(id varchar(255) not null PRIMARY KEY ,content longtext  ,done bit,starred bit,urgent bit,important bit);
CREATE INDEX `id` ON bubble (id); /* 创建索引 */