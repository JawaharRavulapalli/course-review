package com.taskmanagement.courseenrollmen.configuration;

import com.taskmanagement.courseenrollmen.entity.User;
import com.taskmanagement.courseenrollmen.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadMockUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.saveAll(List.of(
                        new User("Jawahar", "jawahar@yopmail.com"),
                        new User("Krishna", "krishna@yopmail.com"),
                        new User("Abhishake", "abhishake@yopmail.com" )
                ));
            }
        };
    }
}
