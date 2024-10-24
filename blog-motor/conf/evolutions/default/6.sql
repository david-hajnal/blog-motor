-- conf/evolutions/default/6.sql

# --- !Ups
UPDATE blog_posts
SET thumbnail_url = 'https://www.idokep.hu/keptar/users/gergo7oth/gergo7oth-cometatlastolgystep3editig.jpg-2024-10-23-14-11-23.jpg.webp'
WHERE id > 0;
