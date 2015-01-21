CREATE SEQUENCE archive_pages_to_refetch_id_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE invisible_users_id_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE online_users_id_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE queries_id_seq1 START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE requests_id_seq1 START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE shout_revisions_id_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE shout_smilies_id_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE shout_words_id_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE smilies_id_seq1 START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE user_categories_id_seq1 START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE words_id_seq1 START WITH 1 INCREMENT BY 50;
CREATE TABLE archive_pages_to_refetch (id INTEGER NOT NULL, added TIMESTAMP NOT NULL, done TIMESTAMP, page INTEGER NOT NULL, PRIMARY KEY (id));
CREATE TABLE invisible_users (id INTEGER NOT NULL, timestamp TIMESTAMP NOT NULL, users INTEGER NOT NULL, PRIMARY KEY (id));
CREATE TABLE online_users (id INTEGER NOT NULL, timestamp TIMESTAMP NOT NULL, user_id INTEGER NOT NULL, PRIMARY KEY (id));
CREATE TABLE queries (id INTEGER NOT NULL, date TIMESTAMP NOT NULL, execution_time DOUBLE NOT NULL, parameters CLOB NOT NULL, query CLOB NOT NULL, request INTEGER NOT NULL, PRIMARY KEY (id));
CREATE TABLE requests (id INTEGER NOT NULL, browser VARCHAR(255) NOT NULL, timestamp TIMESTAMP NOT NULL, ip CLOB NOT NULL, request_time DOUBLE NOT NULL, url CLOB NOT NULL, username VARCHAR(255) NOT NULL, PRIMARY KEY (id));
CREATE TABLE settings (key VARCHAR(50) NOT NULL, value CLOB NOT NULL, PRIMARY KEY (key));
CREATE TABLE shouts (primary_id INTEGER NOT NULL, date TIMESTAMP NOT NULL, day INTEGER NOT NULL, deleted INTEGER NOT NULL, epoch INTEGER NOT NULL, hour INTEGER NOT NULL, id INTEGER NOT NULL, message CLOB NOT NULL, month INTEGER NOT NULL, year INTEGER NOT NULL, user_id INTEGER NOT NULL, PRIMARY KEY (primary_id), CONSTRAINT U_SHOUTS_ID UNIQUE (id, epoch));
CREATE TABLE shout_revisions (id INTEGER NOT NULL, date TIMESTAMP NOT NULL, replaced TIMESTAMP NOT NULL, revision INTEGER NOT NULL, text CLOB NOT NULL, shout INTEGER NOT NULL, user_id INTEGER NOT NULL, PRIMARY KEY (id), CONSTRAINT U_SHT_SNS_SHOUT UNIQUE (shout, revision));
CREATE TABLE shout_smilies (id INTEGER NOT NULL, count INTEGER NOT NULL, shout INTEGER NOT NULL, smiley INTEGER NOT NULL, PRIMARY KEY (id), CONSTRAINT U_SHT_MLS_SHOUT UNIQUE (shout, smiley));
CREATE TABLE shout_words (id INTEGER NOT NULL, count INTEGER NOT NULL, shout INTEGER NOT NULL, word INTEGER NOT NULL, PRIMARY KEY (id), CONSTRAINT U_SHT_RDS_SHOUT UNIQUE (shout, word));
CREATE TABLE smilies (id INTEGER NOT NULL, code VARCHAR(100), filename VARCHAR(100) NOT NULL, meaning CLOB, PRIMARY KEY (id), CONSTRAINT U_SMILIES_CODE UNIQUE (code));
CREATE TABLE users (id INTEGER NOT NULL, name CLOB NOT NULL, category INTEGER NOT NULL, PRIMARY KEY (id));
CREATE TABLE user_categories (id INTEGER NOT NULL, color CLOB NOT NULL, name CLOB NOT NULL, PRIMARY KEY (id));
CREATE TABLE user_credentials (id INTEGER NOT NULL, access_token CLOB NOT NULL, cookie CLOB NOT NULL, password CLOB NOT NULL, securitytoken CLOB NOT NULL, PRIMARY KEY (id));
CREATE TABLE words (id INTEGER NOT NULL, word VARCHAR(100) NOT NULL, PRIMARY KEY (id));
ALTER TABLE online_users ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE queries ADD FOREIGN KEY (request) REFERENCES requests (id);
ALTER TABLE shouts ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE shout_revisions ADD FOREIGN KEY (shout) REFERENCES shouts (primary_id);
ALTER TABLE shout_revisions ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE shout_smilies ADD FOREIGN KEY (shout) REFERENCES shouts (primary_id);
ALTER TABLE shout_smilies ADD FOREIGN KEY (smiley) REFERENCES smilies (id);
ALTER TABLE shout_words ADD FOREIGN KEY (shout) REFERENCES shouts (primary_id);
ALTER TABLE shout_words ADD FOREIGN KEY (word) REFERENCES words (id);
ALTER TABLE users ADD FOREIGN KEY (category) REFERENCES user_categories (id);
ALTER TABLE user_credentials ADD FOREIGN KEY (id) REFERENCES users (id);