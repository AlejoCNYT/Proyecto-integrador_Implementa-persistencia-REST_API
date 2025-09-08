package io.econexion.lab.users.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;         // ✅ ESTE
import org.springframework.data.mongodb.core.mapping.Document;  // ✅
import java.time.Instant;

@Document(collection = "lab_users")
public class LabUser {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)   // ✅ ahora reconoce 'unique'
    private String email;

    private String role;
    private Instant createdAt = Instant.now();

    public LabUser() {}
    public LabUser(String id, String name, String email, String role) {
        this.id = id; this.name = name; this.email = email; this.role = role;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
