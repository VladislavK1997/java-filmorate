package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class User {

    private int id;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    private String login;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Email(message = "Email должен быть корректным")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    @Past(message = "Дата рождения должна быть в прошлом")
    @NotNull(message = "Дата рождения не может быть пустой")
    private LocalDate birthday;

    // Список друзей (ID пользователей)
    private Set<Integer> friends = new HashSet<>();

    // ======= Геттеры и сеттеры =======
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Set<Integer> getFriends() {
        return friends;
    }

    public void setFriends(Set<Integer> friends) {
        this.friends = friends;
    }
}


