package sfedu.railway.models;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
public class User {
    private String id;
    private String surname;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;

    public User(String surname, String name, String phoneNumber, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.surname = surname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }
    
    public User() {
        this.id = UUID.randomUUID().toString();
    }
}
