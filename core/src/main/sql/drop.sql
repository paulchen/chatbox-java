DROP TABLE IF EXISTS archive_pages_to_refetch CASCADE;
DROP TABLE IF EXISTS invisible_users CASCADE;
DROP TABLE IF EXISTS online_users CASCADE;
DROP TABLE IF EXISTS queries CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS settings CASCADE;
DROP TABLE IF EXISTS shouts CASCADE;
DROP TABLE IF EXISTS shout_revisions CASCADE;
DROP TABLE IF EXISTS shout_smilies CASCADE;
DROP TABLE IF EXISTS shout_words CASCADE;
DROP TABLE IF EXISTS smiley_codes CASCADE;
DROP TABLE IF EXISTS smilies CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS user_categories CASCADE;
DROP TABLE IF EXISTS user_credentials CASCADE;
DROP TABLE IF EXISTS words CASCADE;

DROP SEQUENCE IF EXISTS archive_pages_to_refetch_id_seq;
DROP SEQUENCE IF EXISTS invisible_users_id_seq;
DROP SEQUENCE IF EXISTS online_users_id_seq;
DROP SEQUENCE IF EXISTS queries_id_seq1;
DROP SEQUENCE IF EXISTS requests_id_seq1;
DROP SEQUENCE IF EXISTS shout_revisions_id_seq;
DROP SEQUENCE IF EXISTS shout_smilies_id_seq;
DROP SEQUENCE IF EXISTS shout_words_id_seq;
DROP SEQUENCE IF EXISTS smiley_codes_id_seq;
DROP SEQUENCE IF EXISTS smilies_id_seq1;
DROP SEQUENCE IF EXISTS user_categories_id_seq1;
DROP SEQUENCE IF EXISTS words_id_seq1;