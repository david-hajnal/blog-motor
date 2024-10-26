-- conf/evolutions/default/8.sql

# --- !Ups
ALTER TABLE blog_posts ADD COLUMN slug TEXT NOT NULL DEFAULT '';

# --- !Downs
ALTER TABLE blog_posts DROP COLUMN slug;
