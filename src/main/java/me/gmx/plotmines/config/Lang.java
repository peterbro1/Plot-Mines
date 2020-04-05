package me.gmx.plotmines.config;

import me.gmx.plotmines.core.BConfig;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;


public enum Lang {

    PREFIX("&2[&4Plot Mines&2]&r "),
    MSG_ERROR("Error occured, please contact server developer."),
    MSG_PLOTMINE_USAGE("&4Incorrect usage! See /plotmine help."),
    MSG_FAILED_PLACE("&4Your plotmine has failed to place!"),
    MSG_USAGE_SUBCOMMAND("Incorrect usage. Correct usage is %usage%"),
    MSG_INV_FULL("Your inventory is full! Please empty a slot so your plotmine can go into your inventory!"),
    LANG_CONSOLE("Console"),
    MSG_MINE_CONFIRMED("&4You've confirmed the placement of your plotmine!"),
    MSG_PLOTMINE_PLACE("&4You've placed a plotmine! To reset it, right click the top block. To disassemble it, shift right click it!"),
    MSG_CONFIG_RELOADED("Config reloaded."),
    MSG_DONT_OWN("&4You do not own this plotmine!"),
    MSG_PLOTMINE_RIGHTCLICK("You must empty your mine before resetting it! &4To remove the mine, shift right click the top block!"),
    MSG_FAILED_BREAK("You cannot break a plotmine! Please right click the top block."),
    MSG_NOT_EMPTY("&2Your mine must be empty before refilling itself!"),
    MSG_PLAYERONLY("This command can only be executed by a player"),
    MSG_PLAYER_NULL("&4Player could not be found!"),
    PLOTMINE_TITLE("&4Plot&2Mine"),
    MSG_PLOTMINE_RESET("&2Your plotmine has been reset."),
    MSG_NO_PLOTMINES("&4You do not have any active plotmines!"),
    PLOTMINE_LORE("&4Plotmine size: %size%/&3Plotmine type: %type%"),
    MSG_NOACCESS("&4You don't have permission to issue this command!"),
    MSG_MINE_REFILLED("&cYour mine has been refilled!"),
    MSG_PLOTMINE_HELP("&2Insert generic help here.");


    private String defaultValue;
    private static BConfig config;

    Lang(String str){
        defaultValue = str;
    }


    public String getPath() { return name(); }

    public String getDefaultValue() { return this.defaultValue; }

    public String toString() { return fixColors(config.getConfig().getString(getPath())); }

    public static void setConfig(BConfig paramBConfig) {
        config = paramBConfig;
        load();
    }

    public List<String> getStringList(){
        return Arrays.asList(toString().split("/"));
    }

    public String toMsg() {
        boolean bool = true;
        if (bool) {
            return fixColors(config.getConfig().getString(PREFIX.getPath()) + config.getConfig()
                    .getString(getPath()));
        }
        return fixColors(config.getConfig().getString(getPath()));
    }

    public void set( String o){
        config.getConfig().set(getPath(),o);
    }

    private static void load() {
        for (Lang lang : values()) {
            if (config.getConfig().getString(lang.getPath()) == null) {
                config.getConfig().set(lang.getPath(), lang.getDefaultValue());
            }
        }
        config.save();
    }


    private String fixColors(String paramString) {
        if (paramString == null)
            return "";
        return ChatColor.translateAlternateColorCodes('&', paramString);
    }
}
