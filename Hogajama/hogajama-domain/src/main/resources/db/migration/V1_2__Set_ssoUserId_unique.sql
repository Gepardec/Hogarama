SET search_path = "hogajama";

ALTER TABLE ONLY owner
    ADD CONSTRAINT pui_owner_sso_user_id
        UNIQUE (sso_user_id);
