package com.example.examplemod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class HelpCommand {

    static String formattedKeybinds = "";
    static String formattedKeys = "";

    public static void init() {
        StringBuilder formattedKeybindsBuilder = new StringBuilder();
        StringBuilder formattedKeysBuilder = new StringBuilder();

        for (String binding : ChangeKeybindCommand.keybinds) {
            if (!formattedKeybindsBuilder.isEmpty()) formattedKeybindsBuilder.append(", ");
            formattedKeybindsBuilder.append(binding);
        }

        for (String key : ChangeKeybindCommand.keys) {
            if (!formattedKeysBuilder.isEmpty()) formattedKeysBuilder.append(", ");
            formattedKeysBuilder.append(key);
        }

        formattedKeybinds = formattedKeybindsBuilder.toString();
        formattedKeys = formattedKeysBuilder.toString();
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("keychanger_help").executes(HelpCommand::help);

        dispatcher.register(command);
    }

    static int help(CommandContext<CommandSourceStack> commandContext) {


        commandContext.getSource().sendSystemMessage(Component.literal("[§d§lKeychanger§r] Help\n"));
        commandContext.getSource().sendSystemMessage(Component.literal("\nKeybinds: " + formattedKeybinds));
        commandContext.getSource().sendSystemMessage(Component.literal("\nValid Keys: " + formattedKeys));

        return 1;
    }
}
