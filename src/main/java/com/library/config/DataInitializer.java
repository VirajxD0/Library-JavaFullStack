package com.library.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.Category;
import com.library.entity.Member;
import com.library.entity.Publisher;
import com.library.entity.Role;
import com.library.entity.User;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import com.library.repository.CategoryRepository;
import com.library.repository.MemberRepository;
import com.library.repository.PublisherRepository;
import com.library.repository.UserRepository;

import java.time.LocalDate;

/**
 * DataInitializer - Seeds DB with sample data + default users
 * Added JWT/Security: creates admin (ROLE_ADMIN) and user (ROLE_USER) with BCrypt passwords
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(CategoryRepository categoryRepo,
                               AuthorRepository authorRepo,
                               PublisherRepository publisherRepo,
                               BookRepository bookRepo,
                               MemberRepository memberRepo,
                               UserRepository userRepo,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            if (categoryRepo.count() > 0) {
                System.out.println("Database already has data. Skipping initialization.");
                return;
            }

            System.out.println("Seeding database with sample data...");

            Category fiction = categoryRepo.save(new Category("Fiction", "Fictional literature and novels"));
            Category nonFiction = categoryRepo.save(new Category("Non-Fiction", "Non-fictional works and essays"));
            Category tech = categoryRepo.save(new Category("Technology", "Computer Science and Technology"));
            Category science = categoryRepo.save(new Category("Science", "Scientific publications"));
            Category history = categoryRepo.save(new Category("History", "Historical works and accounts"));

            Author james = authorRepo.save(new Author("James", "Gosling", "james@gosling.dev", "Creator of Java programming language"));
            Author joshua = authorRepo.save(new Author("Joshua", "Bloch", "josh@bloch.dev", "Author of Effective Java"));
            Author craig = authorRepo.save(new Author("Craig", "Walls", "craig@walls.dev", "Spring in Action author"));
            Author robert = authorRepo.save(new Author("Robert", "Martin", "robert@clean.dev", "Author of Clean Code"));

            Publisher penguin = publisherRepo.save(new Publisher("Penguin Random House", "New York, USA", "https://penguinrandomhouse.com"));
            Publisher oreilly = publisherRepo.save(new Publisher("O'Reilly Media", "Sebastopol, CA", "https://oreilly.com"));
            Publisher mh = publisherRepo.save(new Publisher("McGraw-Hill", "New York, USA", "https://mheducation.com"));

            Book effectiveJava = new Book("Effective Java", "978-0134685991", 2018, 5, 3,
                    "Best practices for Java programming", "English", tech, oreilly);
            effectiveJava.addAuthor(joshua);
            bookRepo.save(effectiveJava);

            Book springInAction = new Book("Spring in Action", "978-1617294947", 2022, 3, 2,
                    "Comprehensive guide to Spring Framework", "English", tech, oreilly);
            springInAction.addAuthor(craig);
            bookRepo.save(springInAction);

            Book cleanCode = new Book("Clean Code", "978-0132350884", 2008, 6, 5,
                    "A handbook of agile software craftsmanship", "English", tech, penguin);
            cleanCode.addAuthor(robert);
            bookRepo.save(cleanCode);

            Book headFirstJava = new Book("Head First Java", "978-0596009205", 2005, 3, 3,
                    "A brain-friendly guide to Java", "English", tech, oreilly);
            headFirstJava.addAuthor(james);
            bookRepo.save(headFirstJava);

            Book designPatterns = new Book("Design Patterns", "978-0201633610", 1994, 4, 4,
                    "Elements of reusable object-oriented software", "English", tech, penguin);
            designPatterns.addAuthor(james);
            bookRepo.save(designPatterns);

            memberRepo.save(new Member("Alice", "Johnson", "alice@email.com", "555-0101", LocalDate.of(2024, 1, 15)));
            memberRepo.save(new Member("Bob", "Smith", "bob@email.com", "555-0102", LocalDate.of(2024, 2, 20)));
            memberRepo.save(new Member("Charlie", "Brown", "charlie@email.com", "555-0103", LocalDate.of(2024, 3, 10)));

            // ====== Security Users ======
            // Password: Admin@123 (BCrypt hashed)
            if (!userRepo.existsByUsername("admin")) {
                User admin = new User("admin", "admin@library.com", passwordEncoder.encode("Admin@123"), Role.ROLE_ADMIN);
                userRepo.save(admin);
                System.out.println("Created ADMIN user: admin / Admin@123");
            }
            if (!userRepo.existsByUsername("user")) {
                User user = new User("user", "user@library.com", passwordEncoder.encode("User@123"), Role.ROLE_USER);
                userRepo.save(user);
                System.out.println("Created USER user: user / User@123");
            }

            System.out.println("Database seeded successfully!");
            System.out.println("Categories: " + categoryRepo.count());
            System.out.println("Authors: " + authorRepo.count());
            System.out.println("Books: " + bookRepo.count());
            System.out.println("Members: " + memberRepo.count());
            System.out.println("Users: " + userRepo.count());
        };
    }
}
