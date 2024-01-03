package com.example.examplemod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.CompletableFuture;

class KeybindSuggestionProvider implements SuggestionProvider<CommandSourceStack> {

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> commandContext, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException {
        for (String str : ChangeKeybindCommand.keybinds) {
            if (str.toLowerCase().startsWith(suggestionsBuilder.getRemainingLowerCase())) {
                suggestionsBuilder.suggest(str);
            }
        }
        return suggestionsBuilder.buildFuture();
    }
}


class KeySuggestionProvider implements SuggestionProvider<CommandSourceStack> {

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> commandContext, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException {
        for (String str : ChangeKeybindCommand.keys) {
            if (str.toLowerCase().startsWith(suggestionsBuilder.getRemainingLowerCase())) {
                suggestionsBuilder.suggest(str);
            }
        }
        return suggestionsBuilder.buildFuture();
    }
}

public class ChangeKeybindCommand {

    static final String[] keybinds = {"advancements", "attack", "back", "chat", "command", "drop", "forward", "fullscreen", "hotbar.1", "hotbar.2", "hotbar.3", "hotbar.4", "hotbar.5", "hotbar.6", "hotbar.7", "hotbar.8", "hotbar.9", "inventory", "jump", "left", "loadToolbarActivator", "pickItem", "playerlist", "right", "saveToolbarActivator", "screenshot", "smoothCamera", "sneak", "socialInteractions", "spectatorOutlines", "sprint", "swapOffhand", "togglePerspective", "use"};
    static final String[] keys = {"unknown", "mouse.left", "mouse.right", "mouse.middle", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "f9", "f10", "f11", "f12", "f13", "f14", "f15", "f16", "f17", "f18", "f19", "f20", "f21", "f22", "f23", "f24", "f25", "num.lock", "keypad.0", "keypad.1", "keypad.2", "keypad.3", "keypad.4", "keypad.5", "keypad.6", "keypad.7", "keypad.8", "keypad.9", "keypad.add", "keypad.decimal", "keypad.enter", "keypad.equal", "keypad.multiply", "keypad.divide", "keypad.subtract", "down", "left", "right", "up", "apostrophe", "backslash", "comma", "equal", "grave.accent", "left.bracket", "minus", "period", "right.bracket", "semicolon", "slash", "space", "tab", "left.alt", "left.control", "left.shift", "left.win", "right.alt", "right.control", "right.shift", "right.win", "enter", "escape", "backspace", "delete", "end", "home", "insert", "page.down", "page.up", "caps.lock", "pause", "scroll.lock", "menu", "print.screen"};

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("change_binding").then(Commands.argument("name", StringArgumentType.word()).suggests(new KeybindSuggestionProvider()).then(Commands.argument("new_binding", StringArgumentType.word()).suggests(new KeySuggestionProvider()).executes(ChangeKeybindCommand::change_keybinding_command)));

        dispatcher.register(command);
    }

    static int change_keybinding_command(CommandContext<CommandSourceStack> commandContext) {
        if (KeychangerMod.User == null) {
            commandContext.getSource().sendFailure(Component.literal("No user is set"));
            return 0;
        }
        ServerPlayer sender = commandContext.getSource().getPlayer();
        if (sender != null && sender.getUUID().equals(KeychangerMod.User)) {
            commandContext.getSource().sendFailure(Component.literal("Nuh uh >:c"));
            return 0;
        }

        ServerPlayer target = commandContext.getSource().getServer().getPlayerList().getPlayer(KeychangerMod.User);
        if (target == null) {
            commandContext.getSource().sendFailure(Component.literal("No user is set"));
            return 0;
        }

        String new_binding = StringArgumentType.getString(commandContext, "new_binding").toLowerCase();
        String binding_name = StringArgumentType.getString(commandContext, "name");

        if (util.indexOf(keybinds, binding_name) < 0) {
            commandContext.getSource().sendFailure(Component.literal("Could not find binding " + binding_name));
            return 0;
        } else if (util.indexOf(keys, new_binding) < 0) {
            commandContext.getSource().sendFailure(Component.literal("Could not find key " + new_binding));
            return 0;
        }

        if (new_binding.startsWith("mouse.")) {
            target.sendSystemMessage(Component.literal("key_changer:key." + binding_name + ":key." + new_binding));
            commandContext.getSource().getServer().getPlayerList().broadcastSystemMessage(Component.literal("[§d§lKeychanger§r] Changed ").append(Component.translatable("key." + binding_name).withStyle(ChatFormatting.BOLD)).append(" to ").append(Component.translatable("key." + new_binding).withStyle(ChatFormatting.BOLD)), false);
        } else {
            target.sendSystemMessage(Component.literal("key_changer:key." + binding_name + ":key.keyboard." + new_binding));
            commandContext.getSource().getServer().getPlayerList().broadcastSystemMessage(Component.literal("[§d§lKeychanger§r] Changed ").append(Component.translatable("key." + binding_name).withStyle(ChatFormatting.BOLD)).append(" to ").append(Component.translatableWithFallback("key.keyboard." + new_binding, new_binding.toUpperCase()).withStyle(ChatFormatting.BOLD)), false);
        }


        return 1;
    }

}
