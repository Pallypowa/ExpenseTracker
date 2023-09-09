package com.pico.budgetapplication;

import com.pico.budgetapplication.model.*;
import com.pico.budgetapplication.repository.AccountRepository;
import com.pico.budgetapplication.repository.CategoryRepository;
import com.pico.budgetapplication.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class BudgetApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository,
                                        CategoryRepository categoryRepository,
                                        AccountRepository accountRepository){
        return args -> {

            List<Category> categories = new ArrayList<>();
            List<User> users = new ArrayList<>();

            categories.add(new Category(
                    "Utilities",
                    "https:randomurl.com",
                    null
            ));
            categories.add(new Category(
                    "Travel",
                    "https:randomurl.com",
                    null
            ));
            categories.add(new Category(
                    "Groceries",
                    "https:randomurl.com",
                    null
            ));
            categories.add(new Category(
                    "Health",
                    "https:randomurl.com",
                    null
            ));
            categories.add(new Category(
                    "Fun",
                    "https:randomurl.com",
                    null
            ));

            categoryRepository.saveAll(categories);

            User user1 = new User("Pallypowa", "$2a$12$UtKwgMJFo43HD7/t6GY0XeURvVP.yTdLA4TJoeFBTj4dZMRO6WEpK", "pallypowa@pico.com");
            User user2 = new User("Wowfan", "$2a$12$jmCjckfBsGj6PFBVVEn81OQi.rRZWkS1ERePcwZz16X9sZTCF0mtC", "wowfan@pico.com");
            User user3 = new User("Frodo", "$2a$12$6dW7TWKd6wxaHsjyY5hgk.nPz.tm6WyU5.HHhS7GdZyNXAtwr2NLC", "baggins.frodo@pico.com");

            users.add(user1);
            users.add(user2);
            users.add(user3);
            userRepository.saveAll(users);

            Account account = new Account("Main", 980000, user1, "HUF");
            Account account2 = new Account("Main", 12000, user2, "HUF");
            Account account3 = new Account("Main", 120000, user3, "HUF");

            List<Account> accounts = new ArrayList<>();
            accounts.add(account);
            accounts.add(account2);
            accounts.add(account3);
            accountRepository.saveAll(accounts);
        };
    }

}
