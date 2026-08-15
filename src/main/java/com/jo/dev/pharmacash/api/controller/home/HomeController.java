package com.jo.dev.pharmacash.api.controller.home;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/welcome")
    public String welcome() {
        return """
                <html>
                    <body>
                        <h1>Welcome to the Pharmacash API</h1>
                        <p>To get your user information, please visit the <a href="/api/users/me">/me</a> endpoint.</p>
                    </body>
                </html>
                """;
    }
}
