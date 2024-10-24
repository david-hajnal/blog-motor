-- conf/evolutions/default/3.sql

# --- !Ups
INSERT INTO blog_posts (title, content, category, date) VALUES
                                                            ('Post 1', 'Content for post 1.', 'Tech', '2024-10-22T09:00:00+00:00'),
                                                            ('Post 2', 'Content for post 2.', 'Lifestyle', '2024-10-21T08:30:00+00:00'),
                                                            ('Post 3', 'Content for post 3.', 'Health', '2024-10-20T11:15:00+00:00'),
                                                            ('Post 4', 'Content for post 4.', 'Tech', '2024-10-19T14:45:00+00:00'),
                                                            ('Post 5', 'Content for post 5.', 'Education', '2024-10-18T13:00:00+00:00'),
                                                            ('Post 6', 'Content for post 6.', 'Travel', '2024-10-17T16:30:00+00:00'),
                                                            ('Post 7', 'Content for post 7.', 'Tech', '2024-10-16T19:45:00+00:00'),
                                                            ('Post 8', 'Content for post 8.', 'Health', '2024-10-15T21:00:00+00:00'),
                                                            ('Post 9', 'Content for post 9.', 'Lifestyle', '2024-10-14T18:30:00+00:00'),
                                                            ('Post 10', 'Content for post 10.', 'Tech', '2024-10-13T12:00:00+00:00');

# --- !Downs
DELETE FROM blog_posts WHERE title IN
                             ('Post 1', 'Post 2', 'Post 3', 'Post 4', 'Post 5', 'Post 6', 'Post 7', 'Post 8', 'Post 9', 'Post 10');
