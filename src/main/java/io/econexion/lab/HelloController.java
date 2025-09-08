package io.econexion.lab;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloController {
    @GetMapping("/")
    String home() { return "App up ✅"; }
}
