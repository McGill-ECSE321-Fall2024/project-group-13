package group_13.game_store.initializer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Owner;
import group_13.game_store.model.Promotion;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OwnerRepository;
import group_13.game_store.repository.PromotionRepository;
import group_13.game_store.service.AccountService;
import java.util.Map;
import java.util.HashMap;
import java.sql.Date;
import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
    
    @Autowired
    private OwnerRepository ownerRepo;

    @Autowired
    private AccountService accountService;

    @Autowired
    private GameCategoryRepository gameCategoryRepo;

    @Autowired
    private PromotionRepository promotionRepo;

    @Autowired
    private GameRepository gameRepo;

    @PostConstruct
    public void initializeData() {
        // Check if the default owner account already exists, and if not, create it
        if (ownerRepo.findByUsername("owner") == null) {
            String hashedPassword = accountService.hashPassword("ownerPassword");
            Owner owner = new Owner("Owner", "owner", "owner@gmail.com", hashedPassword, "123-456-7890");
            
            // Save to database so that there is a default owner
            ownerRepo.save(owner);
        }

        // ****** WHERE TO ADD DEFAULT GAME CATEGORIES ******
        Map<String, String> categoryDescriptions = new HashMap<>();
        categoryDescriptions.put("Action", "Fast-paced games with exciting gameplay and intense challenges.");
        categoryDescriptions.put("Strategy", "Games that require careful planning and tactical thinking.");
        categoryDescriptions.put("Shooter", "First or third-person games focused on shooting mechanics.");
        categoryDescriptions.put("Adventure", "Games featuring exploration, storylines, and problem-solving.");
        categoryDescriptions.put("Sports", "Games simulating real-world sports or fictional competitions.");
        categoryDescriptions.put("Horror", "Games designed to thrill and scare players with eerie atmospheres.");
        categoryDescriptions.put("Party", "Fun multiplayer games great for gatherings and social play.");
        categoryDescriptions.put("Puzzle", "Games that challenge problem-solving skills and creativity.");
        categoryDescriptions.put("RPG", "Role-playing games with character customization and immersive stories.");
        categoryDescriptions.put("Sandbox", "Open-world games that allow players to create and explore freely.");
        categoryDescriptions.put("Simulation", "Games that simulate real-world activities or fictional scenarios.");
        categoryDescriptions.put("Survival", "Games where players must survive in harsh environments or situations.");

        for (Map.Entry<String, String> entry : categoryDescriptions.entrySet()) {
            String categoryName = entry.getKey();
            String description = entry.getValue();
            
            if (gameCategoryRepo.findByName(categoryName) == null) {
                GameCategory category = new GameCategory(
                    description, 
                    GameCategory.VisibilityStatus.Visible, 
                    categoryName
                );
                gameCategoryRepo.save(category);
            }

        }

        // ****** WHERE TO ADD DEFAULT PROMOTIONS ******
         // Add promotions
    Promotion winterSale = new Promotion(
        30, 
        Date.valueOf("2024-11-01"), 
        Date.valueOf("2025-02-28"), 
        "Winter Sale", 
        "Enjoy a massive 30% discount during the winter season!"
    );

    Promotion christmasSale = new Promotion(
        40, 
        Date.valueOf("2024-12-23"), 
        Date.valueOf("2024-12-29"), 
        "Christmas Sale", 
        "Special limited-time Christmas Sale! 40% off during Christmas week."
    );

    // Save promotions if they don't already exist
    if (promotionRepo.findByTitle("Winter Sale") == null) {
        promotionRepo.save(winterSale);
    }

    if (promotionRepo.findByTitle("Christmas Sale") == null) {
        promotionRepo.save(christmasSale);
    }


    // ****** WHERE TO ADD DEFAULT GAMES ******
    if (gameRepo.findByTitle("Rainbow Six Siege") == null) {
            GameCategory shooterCategory = gameCategoryRepo.findByName("Shooter");
            Game rainbowSixSiege = new Game(
                "Rainbow Six Siege", 
                "A tactical shooter with intense team-based gameplay.", 
                "r6.jpg", 
                50, 
                19.99,
                "Mature", 
                Game.VisibilityStatus.Visible, 
                shooterCategory
            );
            gameRepo.save(rainbowSixSiege);
        }

        if (gameRepo.findByTitle("Rounds") == null) {
            GameCategory shooterCategory = gameCategoryRepo.findByName("Shooter");
            Game rounds = new Game(
                "Rounds", 
                "A fast-paced, card-based shooter with unique power-ups.", 
                "rounds.jpg", 
                30, 
                9.99, 
                "Everyone", 
                Game.VisibilityStatus.Visible, 
                shooterCategory
            );
            gameRepo.save(rounds);
        }

        if (gameRepo.findByTitle("Civilization VI") == null) {
            GameCategory strategyCategory = gameCategoryRepo.findByName("Strategy");
            Game civilizationVI = new Game(
                "Civilization VI", 
                "A strategy game where you build and lead a civilization to greatness.", 
                "civ6.jpg", 
                40, 
                29.99, 
                "Everyone 10+", 
                Game.VisibilityStatus.Visible, 
                strategyCategory
            );
            gameRepo.save(civilizationVI);
        }

        if (gameRepo.findByTitle("Destiny 2") == null) {
            GameCategory shooterCategory = gameCategoryRepo.findByName("Shooter");
            Game destiny2 = new Game(
                "Destiny 2", 
                "A sci-fi multiplayer shooter with immersive PvE and PvP gameplay.", 
                "d2.jpg", 
                60, 
                10.00, 
                "Teen", 
                Game.VisibilityStatus.Visible, 
                shooterCategory
            );
            destiny2.setPromotion(winterSale); 
            gameRepo.save(destiny2);
        }

        if (gameRepo.findByTitle("Steep") == null) {
            GameCategory sportsCategory = gameCategoryRepo.findByName("Sports");
            Game steep = new Game(
                "Steep", 
                "An open-world extreme sports game featuring snowboarding, skiing, and more.", 
                "steep.jpg", 
                20, 
                14.99,
                "Teen", 
                Game.VisibilityStatus.Visible, 
                sportsCategory
            );
            steep.setPromotion(christmasSale); 
            gameRepo.save(steep);
        }
    
        if (gameRepo.findByTitle("Alto's Collection") == null) {
            GameCategory adventureCategory = gameCategoryRepo.findByName("Adventure");
            Game altosCollection = new Game(
                "Alto's Collection", 
                "A relaxing snowboarding adventure with beautiful landscapes.", 
                "alto.jpg", 
                40, 
                9.99,
                "Everyone", 
                Game.VisibilityStatus.Visible, 
                adventureCategory
            );
            altosCollection.setPromotion(winterSale); // Assign Winter Sale promotion
            gameRepo.save(altosCollection);
        }

    }
}
