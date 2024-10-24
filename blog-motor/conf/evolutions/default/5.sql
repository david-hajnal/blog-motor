-- conf/evolutions/default/5.sql

# --- !Ups
CREATE TABLE settings (
                          key TEXT PRIMARY KEY,
                          value TEXT NOT NULL
);

INSERT INTO settings (key, value) VALUES ('header_title', 'My Blog');

# --- !Downs
DROP TABLE settings;
