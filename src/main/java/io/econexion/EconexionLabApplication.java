package io.econexion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // escanea io.econexion.** (incluye io.econexion.lab.users)
public class EconexionLabApplication {
    public static void main(String[] args) {
        SpringApplication.run(EconexionLabApplication.class, args);
    }
}
