package com.main.bitebyte.common;

import com.main.bitebyte.livecooking.Instructor;
import com.main.bitebyte.livecooking.LiveCookingClass;
import com.main.bitebyte.livecooking.LiveCookingClassRepository;
import com.main.bitebyte.recipe.Recipe;
import com.main.bitebyte.recipe.RecipeRepository;
import com.main.bitebyte.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private LiveCookingClassRepository liveCookingClassRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Value("${app.reset-vector-store:false}")
    private boolean resetVectorStore;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        ensureUsersCollectionExists();
        if (resetVectorStore) {
            dropVectorStoreCollection();
        }
        seedAdminUser();
        seedRecipes();
        seedLiveCookingClasses();
    }

    private void ensureUsersCollectionExists() {
        if (!mongoTemplate.collectionExists(com.main.bitebyte.user.User.class)) {
            mongoTemplate.createCollection(com.main.bitebyte.user.User.class);
            logger.info("Created users collection");
        }
    }

    private void dropVectorStoreCollection() {
        try {
            String collectionName = "vector_store";
            if (mongoTemplate.collectionExists(collectionName)) {
                mongoTemplate.dropCollection(collectionName);
                logger.info("Vector store collection dropped successfully");
            } else {
                logger.info("Vector store collection does not exist, no action taken");
            }
        } catch (Exception e) {
            logger.error("Error dropping Vector store collection", e);
        }
    }

    private void seedAdminUser() {
        if (userRepository.existsByUsername("admin")) {
            logger.info("Admin user already exists, skipping seed");
            return;
        }
        if ("admin123".equals(adminPassword)) {
            logger.warn("Admin user is being seeded with the default password. " +
                        "Set app.admin.password in application.properties for production.");
        }
        com.main.bitebyte.user.User admin = new com.main.bitebyte.user.User(
            "admin",
            passwordEncoder.encode(adminPassword),
            "admin@bitebyte.com",
            java.util.Set.of(com.main.bitebyte.user.Role.ADMIN, com.main.bitebyte.user.Role.USER)
        );
        userRepository.save(admin);
        logger.info("Seeded admin user (username: admin)");
    }

    private void seedRecipes() {
        if (recipeRepository.count() > 0) {
            logger.info("Recipes already exist, skipping seed");
            return;
        }

        List<Recipe> recipes = List.of(
            buildRecipe(
                "Spaghetti Carbonara",
                "A classic Italian pasta dish with eggs, cheese, pancetta and pepper.",
                new String[]{"400g spaghetti", "200g pancetta", "4 eggs", "100g Pecorino Romano", "100g Parmesan", "Black pepper", "Salt"},
                new String[]{"Boil pasta in salted water until al dente.", "Fry pancetta until crispy.", "Whisk eggs with grated cheese.", "Drain pasta, reserving some water.", "Off heat, mix pasta with pancetta then egg mixture, adding pasta water to loosen."},
                List.of("Italian", "Pasta", "Classic"), 2, 1, 20, 4,
                "Per serving: 600 kcal, 30g protein, 65g carbs, 22g fat"
            ),
            buildRecipe(
                "Avocado Toast",
                "Simple, nutritious toast topped with creamy avocado and your choice of toppings.",
                new String[]{"2 slices sourdough bread", "1 ripe avocado", "Lemon juice", "Red chilli flakes", "Salt", "Pepper", "2 eggs (optional)"},
                new String[]{"Toast the bread.", "Mash avocado with lemon juice, salt and pepper.", "Spread on toast.", "Top with chilli flakes and a poached egg if desired."},
                List.of("Breakfast", "Healthy", "Quick"), 1, 1, 5, 2,
                "Per serving: 320 kcal, 10g protein, 28g carbs, 20g fat"
            ),
            buildRecipe(
                "Chicken Tikka Masala",
                "Tender chicken in a rich, creamy tomato-based curry sauce.",
                new String[]{"700g chicken breast", "400ml coconut milk", "400g canned tomatoes", "1 onion", "4 garlic cloves", "1 tbsp ginger", "2 tbsp tikka masala paste", "Fresh coriander"},
                new String[]{"Marinate chicken in yoghurt and tikka paste for 1 hour.", "Grill or pan-fry chicken until charred.", "Fry onion, garlic and ginger.", "Add tomatoes and coconut milk, simmer 15 min.", "Add chicken, cook 10 more minutes.", "Garnish with coriander."},
                List.of("Indian", "Curry", "Dinner"), 3, 2, 40, 4,
                "Per serving: 480 kcal, 45g protein, 18g carbs, 24g fat"
            ),
            buildRecipe(
                "Banana Pancakes",
                "Fluffy two-ingredient pancakes that are naturally sweet and gluten-free.",
                new String[]{"2 ripe bananas", "2 eggs", "Pinch of cinnamon", "Butter or oil for frying"},
                new String[]{"Mash bananas thoroughly.", "Beat in eggs and cinnamon.", "Heat butter in a pan over medium heat.", "Pour small rounds of batter and cook 2 min per side."},
                List.of("Breakfast", "Gluten-free", "Quick"), 1, 1, 10, 2,
                "Per serving: 200 kcal, 8g protein, 30g carbs, 6g fat"
            ),
            buildRecipe(
                "Greek Salad",
                "A refreshing Mediterranean salad with feta, olives and crisp vegetables.",
                new String[]{"3 tomatoes", "1 cucumber", "1 red onion", "100g Kalamata olives", "200g feta cheese", "Olive oil", "Dried oregano", "Salt"},
                new String[]{"Chop tomatoes, cucumber and onion.", "Combine in a bowl with olives.", "Top with crumbled feta.", "Drizzle with olive oil and sprinkle oregano."},
                List.of("Salad", "Mediterranean", "Vegetarian"), 1, 1, 0, 4,
                "Per serving: 250 kcal, 9g protein, 10g carbs, 20g fat"
            )
        );

        recipeRepository.saveAll(recipes);
        logger.info("Seeded {} recipes", recipes.size());
    }

    private void seedLiveCookingClasses() {
        if (liveCookingClassRepository.count() > 0) {
            logger.info("Live cooking classes already exist, skipping seed");
            return;
        }

        Instructor marco = new Instructor();
        marco.setId(1L);
        marco.setName("Marco Rossi");
        marco.setEmail("marco@bitebyte.com");

        Instructor priya = new Instructor();
        priya.setId(2L);
        priya.setName("Priya Sharma");
        priya.setEmail("priya@bitebyte.com");

        LocalDateTime now = LocalDateTime.now();

        List<LiveCookingClass> classes = List.of(
            buildClass(1L, "Pasta Making Masterclass", "Learn to make fresh pasta from scratch with Chef Marco.",
                now.plusDays(3).withHour(18).withMinute(0),
                now.plusDays(3).withHour(20).withMinute(0),
                20, "https://zoom.us/j/example1", marco, 120, 20),
            buildClass(2L, "Indian Spices & Curries", "A deep dive into Indian spice blends and classic curry bases with Chef Priya.",
                now.plusDays(7).withHour(17).withMinute(30),
                now.plusDays(7).withHour(19).withMinute(30),
                25, "https://zoom.us/j/example2", priya, 120, 25),
            buildClass(3L, "Quick Weeknight Meals", "Five healthy, delicious dinners you can make in under 30 minutes.",
                now.plusDays(10).withHour(19).withMinute(0),
                now.plusDays(10).withHour(20).withMinute(30),
                30, "https://zoom.us/j/example3", marco, 90, 30)
        );

        liveCookingClassRepository.saveAll(classes);
        logger.info("Seeded {} live cooking classes", classes.size());
    }

    private Recipe buildRecipe(String name, String description, String[] ingredients,
                                String[] instructions, List<String> tags, int difficulty,
                                int prepTime, int cookTime, int servings, String nutritionalInfo) {
        Recipe r = new Recipe();
        r.setName(name);
        r.setDescription(description);
        r.setIngredients(ingredients);
        r.setInstructions(instructions);
        r.setTags(tags);
        r.setDifficulty(difficulty);
        r.setPreparationTime(prepTime);
        r.setCookingTime(cookTime);
        r.setServings(servings);
        r.setNutritionalInfo(nutritionalInfo);
        r.setPersonal(false);
        return r;
    }

    private LiveCookingClass buildClass(Long id, String title, String description,
                                        LocalDateTime start, LocalDateTime end,
                                        int maxParticipants, String zoomLink,
                                        Instructor instructor, int duration, int capacity) {
        LiveCookingClass c = new LiveCookingClass();
        c.setId(id);
        c.setTitle(title);
        c.setDescription(description);
        c.setStartTime(start);
        c.setEndTime(end);
        c.setDateTime(start);
        c.setMaxParticipants(maxParticipants);
        c.setZoomLink(zoomLink);
        c.setInstructor(instructor);
        c.setDuration(duration);
        c.setCapacity(capacity);
        c.setAttendeeCount(0);
        return c;
    }
}
