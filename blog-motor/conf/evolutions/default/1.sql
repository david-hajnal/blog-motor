-- conf/evolutions/default/1.sql

# --- !Ups
CREATE TABLE blog_posts (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            title TEXT NOT NULL,
                            content TEXT NOT NULL
);

INSERT INTO blog_posts (title, content) VALUES ('First Post', 'This is the content of the first post.');
INSERT INTO blog_posts (title, content) VALUES ('Second Post', 'This is the content of the second post.');
INSERT INTO blog_posts (title, content) VALUES ('Third Post', 'This is the content of the third post.');

# --- !Downs
DROP TABLE blog_posts;
