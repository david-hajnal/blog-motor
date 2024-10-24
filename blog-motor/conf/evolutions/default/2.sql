-- conf/evolutions/default/2.sql

# --- !Ups
ALTER TABLE blog_posts ADD COLUMN category TEXT NOT NULL DEFAULT 'Uncategorized';
ALTER TABLE blog_posts ADD COLUMN date TEXT NOT NULL DEFAULT '2024-01-01T00:00:00+00:00';

UPDATE blog_posts SET category = 'Tech', date = '2024-10-22T10:00:00+00:00' WHERE id = 1;
UPDATE blog_posts SET category = 'Lifestyle', date = '2024-10-21T11:30:00+00:00' WHERE id = 2;
UPDATE blog_posts SET category = 'Health', date = '2024-10-20T15:45:00+00:00' WHERE id = 3;

# --- !Downs
ALTER TABLE blog_posts DROP COLUMN category;
ALTER TABLE blog_posts DROP COLUMN date;
