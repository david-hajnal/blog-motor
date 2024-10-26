-- conf/evolutions/default/9.sql

# --- !Ups
-- Update existing entries with slugs based on the title

UPDATE blog_posts
SET slug = lower(replace(title, ' ', '-'));

# --- !Downs
-- Revert slugs to NULL if you roll back
UPDATE blog_posts
SET slug = NULL
WHERE slug IS NOT NULL;
