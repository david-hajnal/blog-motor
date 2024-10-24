-- conf/evolutions/default/4.sql

# --- !Ups
ALTER TABLE blog_posts ADD COLUMN thumbnail_url TEXT NOT NULL DEFAULT '';

UPDATE blog_posts SET thumbnail_url = 'https://www.idokep.hu/keptar/users/gergo7oth/gergo7oth-cometatlastolgystep3editig.jpg-2024-10-23-14-11-23.jpg.webp' WHERE id = 9;

# --- !Downs
ALTER TABLE blog_posts DROP COLUMN thumbnail_url;
