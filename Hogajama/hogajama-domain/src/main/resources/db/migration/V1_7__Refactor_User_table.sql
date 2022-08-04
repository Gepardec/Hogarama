SET search_path = "hogajama";

alter table owner rename column sso_user_id to key;

alter table owner rename to "user";

alter table unit rename column owner_id to user_id;

alter sequence seq_owner_id rename to seq_user_id;


