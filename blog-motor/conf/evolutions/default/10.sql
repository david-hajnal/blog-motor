-- conf/evolutions/default/10.sql

# --- !Ups
UPDATE blog_posts
SET thumbnail_url = '0'
WHERE id > 0;
