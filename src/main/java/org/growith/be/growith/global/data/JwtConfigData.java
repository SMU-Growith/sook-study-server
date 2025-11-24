package org.growith.be.growith.global.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
@NoArgsConstructor
@AllArgsConstructor
public class JwtConfigData {

    private String secret;
    private Time time;

//    @Setter
//    @Getter
    public static class Time {
        private long accessToken;
        private long refreshToken;
    }
}
