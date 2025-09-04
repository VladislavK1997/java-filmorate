## Схема базы данных
table/filmorate_db.png

### Примеры SQL-запросов

Получение всех фильмов:
```sql
SELECT * FROM films;
SELECT * FROM users;
Топ-5 популярных фильмов:

SELECT f.id, f.name, COUNT(l.user_id) AS likes
FROM films f
LEFT JOIN likes l ON f.id = l.film_id
GROUP BY f.id
ORDER BY likes DESC
LIMIT 5;
Список общих друзей двух пользователей:

SELECT u.id, u.name
FROM users u
JOIN friendships f1 ON u.id = f1.friend_id AND f1.user_id = :user1_id
JOIN friendships f2 ON u.id = f2.friend_id AND f2.user_id = :user2_id
WHERE f1.status = 'подтверждённая' AND f2.status = 'подтверждённая';