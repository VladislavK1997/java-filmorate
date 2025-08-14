package ru.yandex.practicum.filmorate.model;

import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration; // в минутах
    private Set<Long> likes = new HashSet<>();

    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Set<Long> getLikes() {
        return likes;
    }

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }

    public boolean isValid() {
        if (!StringUtils.hasText(this.name)) {
            return false;
        }

        if (this.description != null && this.description.length() > 200) {
            return false;
        }

        if (this.releaseDate != null && this.releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            return false;
        }

        if (this.duration <= 0) {
            return false;
        }

        return true;
    }
}
