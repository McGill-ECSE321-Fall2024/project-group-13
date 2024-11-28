package group_13.game_store.initializer;


import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import group_13.game_store.model.Address;
import group_13.game_store.model.Customer;
import group_13.game_store.model.Owner;
import group_13.game_store.model.PaymentInformation;
import group_13.game_store.repository.AddressRepository;
import group_13.game_store.repository.CustomerRepository;
import group_13.game_store.repository.OwnerRepository;
import group_13.game_store.repository.PaymentInformationRepository;
import group_13.game_store.model.Game;
import group_13.game_store.model.GameCategory;
import group_13.game_store.model.Promotion;
import group_13.game_store.model.UserAccount;
import group_13.game_store.repository.GameCategoryRepository;
import group_13.game_store.repository.GameRepository;
import group_13.game_store.repository.OwnerRepository;
import group_13.game_store.repository.PromotionRepository;
import group_13.game_store.repository.UserAccountRepository;
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
    private AddressRepository addressRepo;

    @Autowired
    private PaymentInformationRepository paymentInfoRepo;

    @Autowired
    private CustomerRepository customerRepo;
  
    @Autowired
    private GameCategoryRepository gameCategoryRepo;

    @Autowired
    private PromotionRepository promotionRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private UserAccountRepository userAccountRepo;

    @PostConstruct
    public void initializeData() {
        // Create a default guest with permission level 0
        if (userAccountRepo.findByUsername("guest") == null) {
            UserAccount guest = new UserAccount("guest", "guest", "guest@guest.guest", "guest", "098-765-4321");
            guest.setPermissionLevel(0);
            userAccountRepo.save(guest);
        }

        // Check if the default owner account already exists, and if not, create it
        if (ownerRepo.findByUsername("owner") == null) {
            String hashedPassword = accountService.hashPassword("own3rPassword");
            Owner owner = new Owner("Owner", "owner", "owner@gmail.com", hashedPassword, "123-456-7890");
            
            // Save to database so that there is a default owner
            ownerRepo.save(owner);
        }

        if (customerRepo.findByUsername("defaultCustomer") == null) {

            Address savedAddress = new Address("Sherbrooke St W", "H3A0G4", 845, "Montreal", "Quebec", "Canada", 0);
            addressRepo.save(savedAddress);

            PaymentInformation savedPaymentInformation = new PaymentInformation("1234123412341234", "Default Customer",
                Date.valueOf("2027-10-11"), 123, savedAddress);
            paymentInfoRepo.save(savedPaymentInformation);

            String hashedPassword = accountService.hashPassword("custom3rPassword");
            Customer defaultCustomer = new Customer("Default Customer", "defaultCustomer", "customer@gmail.com", hashedPassword, "123-456-7890");

            defaultCustomer.setAddress(savedAddress);
            defaultCustomer.setPaymentInformation(savedPaymentInformation);
            
            customerRepo.save(defaultCustomer);
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

        if (gameRepo.findByTitle("Among Us") == null) {
            GameCategory partyCategory = gameCategoryRepo.findByName("Party");
            Game amongUs = new Game(
                "Among Us", 
                "A social deduction game where you work together to find the impostor.", 
                "amongus.webp", 
                50, 
                4.99,
                "Everyone 10+", 
                Game.VisibilityStatus.Visible, 
                partyCategory
            );
            gameRepo.save(amongUs);
        }

        if (gameRepo.findByTitle("Minecraft") == null) {
            GameCategory sandboxCategory = gameCategoryRepo.findByName("Sandbox");
            Game minecraft = new Game(
                "Minecraft", 
                "A creative sandbox game where you can build and explore infinite worlds.", 
                "minecraft.jpg", 
                100, 
                19.99,
                "Everyone", 
                Game.VisibilityStatus.Visible, 
                sandboxCategory
            );
            gameRepo.save(minecraft);
        }

        if (gameRepo.findByTitle("Bloons TD 6") == null) {
            GameCategory strategyCategory = gameCategoryRepo.findByName("Strategy");
            Game bloonsTD6 = new Game(
                "Bloons TD 6", 
                "A tower defense game with monkeys, balloons, and tons of fun challenges.", 
                "bloons.jpg", 
                30, 
                9.99,
                "Everyone", 
                Game.VisibilityStatus.Visible, 
                strategyCategory
            );
            gameRepo.save(bloonsTD6);
        }

        if (gameRepo.findByTitle("Deep Rock Galactic") == null) {
            GameCategory shooterCategory = gameCategoryRepo.findByName("Shooter");
            Game deepRockGalactic = new Game(
                "Deep Rock Galactic", 
                "A cooperative FPS game where you mine, fight, and explore dangerous caves.", 
                "deeprock.jpg", 
                20, 
                29.99,
                "Teen", 
                Game.VisibilityStatus.Visible, 
                shooterCategory
            );
            gameRepo.save(deepRockGalactic);
        }

        if (gameRepo.findByTitle("Risk of Rain 2") == null) {
            GameCategory shooterCategory = gameCategoryRepo.findByName("Shooter");
            Game riskOfRain2 = new Game(
                "Risk of Rain 2", 
                "A roguelike shooter with challenging enemies and powerful items.", 
                "riskofrain.webp", 
                30, 
                24.99,
                "Teen", 
                Game.VisibilityStatus.Visible, 
                shooterCategory
            );
            gameRepo.save(riskOfRain2);
        }

        if (gameRepo.findByTitle("Terraria") == null) {
            GameCategory sandboxCategory = gameCategoryRepo.findByName("Sandbox");
            Game terraria = new Game(
                "Terraria", 
                "An action-adventure sandbox game with exploration, crafting, and building.", 
                "terraria.jpg", 
                50, 
                9.99,
                "Everyone 10+", 
                Game.VisibilityStatus.Visible, 
                sandboxCategory
            );
            gameRepo.save(terraria);
        }

        if (gameRepo.findByTitle("Zelda Breath of the Wild") == null) {
            GameCategory adventureCategory = gameCategoryRepo.findByName("Adventure");
            Game zeldaBreathOfTheWild = new Game(
                "Zelda Breath of the Wild", 
                "An open-world adventure game with exploration, puzzles, and epic battles.", 
                "zelda.jpg", 
                30, 
                59.99,
                "Everyone 10+", 
                Game.VisibilityStatus.Visible, 
                adventureCategory
            );
            gameRepo.save(zeldaBreathOfTheWild);
        }

        if (gameRepo.findByTitle("Lego Star Wars") == null) {
            GameCategory adventureCategory = gameCategoryRepo.findByName("Adventure");
            Game legoStarWars = new Game(
                "Lego Star Wars", 
                "A fun and humorous adventure game set in the Star Wars universe.", 
                "lego.jpg", 
                40, 
                19.99,
                "Everyone 10+", 
                Game.VisibilityStatus.Visible, 
                adventureCategory
            );
            gameRepo.save(legoStarWars);
        }

        if (gameRepo.findByTitle("Golf With Your Friends") == null) {
            GameCategory sportsCategory = gameCategoryRepo.findByName("Sports");
            Game golfWithYourFriends = new Game(
                "Golf With Your Friends", 
                "A fun and challenging mini-golf game with friends.", 
                "golf.jpg", 
                20, 
                14.99,
                "Everyone", 
                Game.VisibilityStatus.Visible, 
                sportsCategory
            );
            gameRepo.save(golfWithYourFriends);
        }

        if (gameRepo.findByTitle("Rocket League") == null) {
            GameCategory sportsCategory = gameCategoryRepo.findByName("Sports");
            Game rocketLeague = new Game(
                "Rocket League", 
                "A high-flying sports game with rocket-powered cars and soccer.", 
                "rocketleague.jpg", 
                50, 
                19.99,
                "Everyone", 
                Game.VisibilityStatus.Visible, 
                sportsCategory
            );
            gameRepo.save(rocketLeague);
        }

        if (gameRepo.findByTitle("FIFA 24") == null) {
            GameCategory sportsCategory = gameCategoryRepo.findByName("Sports");
            Game fifa22 = new Game(
                "FIFA 24", 
                "The latest installment in the FIFA series with updated teams and gameplay.", 
                "fifa24.jpeg", 
                60, 
                59.99,
                "Everyone", 
                Game.VisibilityStatus.Visible, 
                sportsCategory
            );
            gameRepo.save(fifa22);
        }

        if (gameRepo.findByTitle("NBA 2K24") == null) {
            GameCategory sportsCategory = gameCategoryRepo.findByName("Sports");
            Game nba2k22 = new Game(
                "NBA 2K24", 
                "The latest basketball simulation game with updated rosters and features.", 
                "nba2k24.jpeg", 
                40, 
                59.99,
                "Everyone", 
                Game.VisibilityStatus.Visible, 
                sportsCategory
            );
            gameRepo.save(nba2k22);
        }

        if (gameRepo.findByTitle("Dying Light" ) == null) {
            GameCategory horrorCategory = gameCategoryRepo.findByName("Horror");
            Game dyingLight = new Game(
                "Dying Light", 
                "A first-person survival horror game set in a zombie-infested open world.", 
                "dyinglight.jpg", 
                30, 
                19.99,
                "Mature", 
                Game.VisibilityStatus.Visible, 
                horrorCategory
            );
            gameRepo.save(dyingLight);
        }

        if (gameRepo.findByTitle("Brawlhalla") == null) {
            GameCategory partyCategory = gameCategoryRepo.findByName("Party");
            Game brawlhalla = new Game(
                "Brawlhalla", 
                "A platform fighting game with online and local multiplayer.", 
                "brawlhalla.jpg", 
                50, 
                1.99,
                "Everyone 10+", 
                Game.VisibilityStatus.Visible, 
                partyCategory
            );
            gameRepo.save(brawlhalla);
        }

        if (gameRepo.findByTitle("Super Smash Bros") == null) {
            GameCategory partyCategory = gameCategoryRepo.findByName("Party");
            Game superSmashBrosUltimate = new Game(
                "Super Smash Bros", 
                "A crossover fighting game with iconic characters from Nintendo and beyond.", 
                "smashbros.jpeg",  
                40, 
                59.99,
                "Everyone 10+", 
                Game.VisibilityStatus.Visible, 
                partyCategory
            );
            gameRepo.save(superSmashBrosUltimate);
        }

        if (gameRepo.findByTitle("Mario Kart 8 Deluxe") == null) {
            GameCategory partyCategory = gameCategoryRepo.findByName("Party");
            Game marioKart8Deluxe = new Game(
                "Mario Kart 8 Deluxe", 
                "A fun and competitive racing game with classic Nintendo characters.", 
                "mariokart.jpg", 
                30, 
                59.99,
                "Everyone", 
                Game.VisibilityStatus.Visible, 
                partyCategory
            );
            gameRepo.save(marioKart8Deluxe);
        }

        if (gameRepo.findByTitle("Phasmophobia") == null) {
            GameCategory horrorCategory = gameCategoryRepo.findByName("Horror");
            Game phasmophobia = new Game(
                "Phasmophobia", 
                "A cooperative horror game where you hunt ghosts with friends.", 
                "phasmophobia.jpg", 
                20, 
                13.99,
                "Teen", 
                Game.VisibilityStatus.Visible, 
                horrorCategory
            );
            gameRepo.save(phasmophobia);
        }

        if (gameRepo.findByTitle("Farming Simulator 24") == null) {
            GameCategory simulationCategory = gameCategoryRepo.findByName("Simulation");
            Game farmingSimulator22 = new Game(
                "Farming Simulator 24", 
                "A realistic farming simulation game with new crops, animals, and vehicles.", 
                "farmingsim24.jpg", 
                30, 
                39.99,
                "Everyone", 
                Game.VisibilityStatus.Visible, 
                simulationCategory
            );
            gameRepo.save(farmingSimulator22);
        }

        if (gameRepo.findByTitle("Overwatch 2") == null) {
            GameCategory shooterCategory = gameCategoryRepo.findByName("Shooter");
            Game overwatch = new Game(
                "Overwatch 2", 
                "A team-based hero shooter with diverse characters and exciting gameplay.", 
                "overwatch.webp", 
                50, 
                19.99,
                "Teen", 
                Game.VisibilityStatus.Visible, 
                shooterCategory
            );
            gameRepo.save(overwatch);
        }

        if (gameRepo.findByTitle("The Sims 4") == null) {
            GameCategory simulationCategory = gameCategoryRepo.findByName("Simulation");
            Game theSims4 = new Game(
                "The Sims 4", 
                "A life simulation game where you create and control virtual people.", 
                "sims4.jpg", 
                40, 
                39.99,
                "Teen", 
                Game.VisibilityStatus.Visible, 
                simulationCategory
            );
            gameRepo.save(theSims4);
        }

        if (gameRepo.findByTitle("Halo Infinite") == null) {
            GameCategory shooterCategory = gameCategoryRepo.findByName("Shooter");
            Game haloInfinite = new Game(
                "Halo Infinite", 
                "The latest installment in the Halo series with epic sci-fi battles.", 
                "halo.jpg", 
                60, 
                59.99,
                "Teen", 
                Game.VisibilityStatus.Visible, 
                shooterCategory
            );
            gameRepo.save(haloInfinite);
        }

        if (gameRepo.findByTitle("Animal Crossing") == null) {
            GameCategory simulationCategory = gameCategoryRepo.findByName("Simulation");
            Game animalCrossingNewHorizons = new Game(
                "Animal Crossing", 
                "A relaxing life simulation game where you build and customize your island.", 
                "animalcrossing.jpg", 
                50, 
                59.99,
                "Everyone", 
                Game.VisibilityStatus.Visible, 
                simulationCategory
            );
            gameRepo.save(animalCrossingNewHorizons);

        
    }
}
}
