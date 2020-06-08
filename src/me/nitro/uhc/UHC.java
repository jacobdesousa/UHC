package me.nitro.uhc;

import me.nitro.uhc.features.*;
import me.nitro.uhc.modules.*;
import me.nitro.uhc.scenarios.*;
import me.nitro.uhc.scoreboard.UHCScoreboardManager;
import me.nitro.uhc.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class UHC extends JavaPlugin {

    private File langFile;
    private static FileConfiguration lang;
    private GameManager gameManager;
    private Config config;
    private ScenarioGUI scenarioGUI;
    public static Border border;
    private AppleRates appleRates;
    private DeathMessages deathMessages;
    private FoundDiamonds foundDiamonds;
    private Report report;
    private Revive revive;
    public static PVP pvp;
    private GiveAll giveAll;
    private Scatter scatter;
    private TeamManager teamManager;
    private DeathLightning deathLightning;
    private Absorption absorption;
    private BowHealth bowHealth;
    private GhastTears ghastTears;
    private GoldenHeads goldenHeads;
    private HardRecipes hardRecipes;
    private HeadDrop headDrop;
    private HealthCommand healthCommand;
    private HostileMobs hostileMobs;
    private HelpOp helpOp;
    private KillCounter killCounter;
    private LogoutTimer logoutTimer;
    private NaturalRegeneration naturalRegeneration;
    private NotchApples notchApples;
    private PearlDamage pearlDamage;
    private Permaday permaday;
    private Nether nether;
    //private TabHealth tabHealth;
    private TimerCommand timerCommand;
    private UHCScoreboardManager uhcScoreboardManager;
    private Whitelist whitelist;
    private WitchSpawns witchSpawns;

    private CutClean cutClean;
    private Timber timber;
    private NoClean noClean;
    private HasteyBoys hasteyBoys;
    private Backpacks backpacks;

    public void onEnable() {
        createLangConfig();
        variableInit();
        registerCommands();
        registerListeners();
        LogoutTimer.initLogoutTimer();
        //TabHealth.initTabHealth();
        UHCScoreboardManager.initScoreboard();
    }

    public void onDisable() {
        try {
            lang.save(langFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        getCommand("game").setExecutor(gameManager);
        getCommand("uhc").setExecutor(config);
        getCommand("config").setExecutor(config);
        getCommand("scenarios").setExecutor(scenarioGUI);

        getCommand("applerates").setExecutor(appleRates);
        getCommand("report").setExecutor(report);
        getCommand("revive").setExecutor(revive);
        getCommand("pvp").setExecutor(pvp);
        getCommand("giveall").setExecutor(giveAll);
        getCommand("scatter").setExecutor(scatter);
        getCommand("sca").setExecutor(scatter);
        getCommand("sc").setExecutor(teamManager);
        getCommand("team").setExecutor(teamManager);
        getCommand("deathlightning").setExecutor(deathLightning);
        getCommand("absorption").setExecutor(absorption);
        getCommand("ghasttears").setExecutor(ghastTears);
        getCommand("goldenheads").setExecutor(goldenHeads);
        getCommand("hardrecipes").setExecutor(hardRecipes);
        getCommand("headdrop").setExecutor(headDrop);
        getCommand("h").setExecutor(healthCommand);
        getCommand("health").setExecutor(healthCommand);
        getCommand("help").setExecutor(helpOp);
        getCommand("helpop").setExecutor(helpOp);
        getCommand("kills").setExecutor(killCounter);
        getCommand("hostilemobs").setExecutor(hostileMobs);
        getCommand("naturalregeneration").setExecutor(naturalRegeneration);
        getCommand("notchapples").setExecutor(notchApples);
        getCommand("pearldamage").setExecutor(pearlDamage);
        getCommand("permaday").setExecutor(permaday);
        getCommand("nether").setExecutor(nether);
        getCommand("gametime").setExecutor(timerCommand);
        getCommand("whitelist").setExecutor(whitelist);
        getCommand("wl").setExecutor(whitelist);
        getCommand("witchspawns").setExecutor(witchSpawns);

        getCommand("cutclean").setExecutor(cutClean);
        getCommand("timber").setExecutor(timber);
        getCommand("noclean").setExecutor(noClean);
        getCommand("hasteyboys").setExecutor(hasteyBoys);
        getCommand("backpacks").setExecutor(backpacks);
        getCommand("bp").setExecutor(backpacks);
        getCommand("backpack").setExecutor(backpacks);

    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(gameManager, this);
        pm.registerEvents(config, this);
        pm.registerEvents(scenarioGUI, this);

        pm.registerEvents(appleRates, this);
        pm.registerEvents(deathMessages, this);
        pm.registerEvents(foundDiamonds, this);
        pm.registerEvents(report, this);
        pm.registerEvents(revive, this);
        pm.registerEvents(pvp, this);
        pm.registerEvents(new JoinMessage(), this);
        pm.registerEvents(new QuitMessage(), this);
        pm.registerEvents(new CustomChat(this), this);
        pm.registerEvents(scatter, this);
        pm.registerEvents(deathLightning, this);
        pm.registerEvents(absorption, this);
        pm.registerEvents(bowHealth, this);
        pm.registerEvents(ghastTears, this);
        pm.registerEvents(goldenHeads, this);
        pm.registerEvents(hardRecipes, this);
        pm.registerEvents(headDrop, this);
        pm.registerEvents(hostileMobs, this);
        pm.registerEvents(killCounter, this);
        pm.registerEvents(logoutTimer, this);
        pm.registerEvents(naturalRegeneration, this);
        pm.registerEvents(notchApples, this);
        pm.registerEvents(pearlDamage, this);
        pm.registerEvents(nether, this);
        //pm.registerEvents(tabHealth, this);
        pm.registerEvents(uhcScoreboardManager, this);
        pm.registerEvents(whitelist, this);
        pm.registerEvents(witchSpawns, this);

        pm.registerEvents(cutClean, this);
        pm.registerEvents(timber, this);
        pm.registerEvents(noClean, this);
        pm.registerEvents(hasteyBoys, this);
    }

    private void variableInit() {
        Whitelist.enable();
        Whitelist.whitelist = new HashSet<>();
        gameManager = new GameManager(this);
        config = new Config();
        scenarioGUI = new ScenarioGUI();
        border = new Border(this);

        appleRates = new AppleRates();
        deathMessages = new DeathMessages();
        foundDiamonds = new FoundDiamonds();
        report = new Report();
        revive = new Revive();
        pvp = new PVP();
        giveAll = new GiveAll();
        scatter = new Scatter();
        teamManager = new TeamManager();
        deathLightning = new DeathLightning();
        absorption = new Absorption(this);
        bowHealth = new BowHealth(this);
        ghastTears = new GhastTears();
        goldenHeads = new GoldenHeads();
        hardRecipes = new HardRecipes();
        headDrop = new HeadDrop();
        healthCommand = new HealthCommand();
        helpOp = new HelpOp(this);
        killCounter = new KillCounter();
        logoutTimer = new LogoutTimer(this);
        hostileMobs = new HostileMobs();
        naturalRegeneration = new NaturalRegeneration();
        notchApples = new NotchApples();
        pearlDamage = new PearlDamage();
        permaday = new Permaday();
        nether = new Nether();
        //tabHealth = new TabHealth();
        timerCommand = new TimerCommand();
        uhcScoreboardManager = new UHCScoreboardManager(this);
        whitelist = new Whitelist();
        witchSpawns = new WitchSpawns();

        cutClean = new CutClean();
        timber = new Timber();
        noClean = new NoClean(this);
        hasteyBoys = new HasteyBoys();
        backpacks = new Backpacks();
    }

    private void createLangConfig() {
        langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            langFile.getParentFile().mkdirs();
            saveResource("lang.yml", false);
        }

        lang = new YamlConfiguration();
        try {
            lang.load(langFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static String getPrefix() {
        return Utils.colour(lang.get("prefix").toString());
    }

    public static String getMessage(String message) {
        return getPrefix() + Utils.colour(lang.get(message).toString());
    }

    public static String getRawMessage(String id) {
        return Utils.colour(lang.get(id).toString());
    }

    public UHC getInstance() {
        return this;
    }
}
