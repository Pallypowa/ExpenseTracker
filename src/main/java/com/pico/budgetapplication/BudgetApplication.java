package com.pico.budgetapplication;

import com.pico.budgetapplication.model.Category;
import com.pico.budgetapplication.model.Expense;
import com.pico.budgetapplication.model.User;
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
    CommandLineRunner commandLineRunner(UserRepository userRepository, CategoryRepository categoryRepository){
        return args -> {

            List<Category> categories = new ArrayList<>();
            List<User> users = new ArrayList<>();
            List<Expense> expenses1 = new ArrayList<>();
            List<Expense> expenses2 = new ArrayList<>();
            List<Expense> expenses3 = new ArrayList<>();

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

            User user1 = new User("Pallypowa", "$2a$12$NkhU1V4IJOrNKepXmo7QuekxS2r/OsQxEUzFWrAoY4J8VQh7mS4Du", "pallypowa@pico.com");
            User user2 = new User("Wowfan", "password", "wowfan@pico.com");
            User user3 = new User("Frodo_Baggins", "password", "baggins.frodo@pico.com");

            Expense expense1 = new Expense(1385,
                    LocalDateTime.now(),
                    "HUF",
                    "Just a daily cinema", user1,
                    categories.get(0));
            Expense expense2 = new Expense(25000,
                    LocalDateTime.now(),
                    "HUF",
                    "Electricity bill", user1,
                    categories.get(4));
            Expense expense3 = new Expense(48900,
                    LocalDateTime.now(),
                    "HUF",
                    "User2", user2,
                    categories.get(4));

            Expense expense4 = new Expense(1500000,
                    LocalDateTime.now(),
                    "HUF",
                    "User 3", user3,
                    categories.get(4));

            expenses1.add(expense1);
            expenses1.add(expense2);
            expenses2.add(expense3);
            expenses3.add(expense4);
            user1.setExpenses(expenses1);
            user2.setExpenses(expenses2);
            user3.setExpenses(expenses3);

            users.add(user1);
            users.add(user2);
            users.add(user3);
            userRepository.saveAll(users);

        };
    }

}
