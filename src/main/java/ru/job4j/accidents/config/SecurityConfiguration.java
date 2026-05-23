package ru.job4j.accidents.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private final DataSource ds;

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(ds);
        if (!userDetailsManager.userExists("user")) {
            UserDetails user = User.builder()
                    .username("user")
                    .password(encoder.encode("123456"))
                    .roles("USER")
                    .build();
            userDetailsManager.createUser(user);
        }
        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/login").permitAll()
                        .anyRequest().hasAnyRole("ADMIN", "USER"))
                .formLogin(customizer -> customizer
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(customizer -> customizer
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}