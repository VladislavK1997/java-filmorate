SELECT f.*, COUNT(l.user_id) as likes_count
FROM films f
LEFT JOIN likes l ON f.id = l.film_id
GROUP BY f.id
ORDER BY likes_count DESC
LIMIT :count;

SELECT u.*
FROM users u
JOIN friendships f1 ON u.id = f1.friend_id AND f1.user_id = :userId
JOIN friendships f2 ON u.id = f2.friend_id AND f2.user_id = :otherId
WHERE f1.status = 'CONFIRMED' AND f2.status = 'CONFIRMED';

SELECT u.*
FROM users u
JOIN friendships f ON u.id = f.friend_id
WHERE f.user_id = :userId AND f.status = 'PENDING';

UPDATE friendships
SET status = 'CONFIRMED'
WHERE user_id = :userId AND friend_id = :friendId;