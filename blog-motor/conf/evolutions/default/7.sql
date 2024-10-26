-- conf/evolutions/default/7.sql

# --- !Ups
CREATE TABLE images (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        mime_type TEXT NOT NULL,
                        data BLOB NOT NULL
);

# --- !Downs
DROP TABLE images;
