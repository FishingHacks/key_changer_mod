package com.example.examplemod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ChangeUserCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> change_user_command = Commands.literal("change_user")
                .then(Commands.argument("name", EntityArgument.player()).executes(ChangeUserCommand::change_user_command));

        LiteralArgumentBuilder<CommandSourceStack> reset_user_command = Commands.literal("reset_user")
                .executes(ChangeUserCommand::reset_user_command);

        dispatcher.register(change_user_command);
        dispatcher.register(reset_user_command);
    }

    static int change_user_command(CommandContext<CommandSourceStack> commandContext) {
        ServerPlayer sender = commandContext.getSource().getPlayer();
        if (sender == null) {
            return 0;
        }

        try {
            ServerPlayer player = EntityArgument.getPlayer(commandContext, "name");
            KeychangerMod.User = player.getUUID();
            commandContext.getSource().getServer().getPlayerList().broadcastSystemMessage(Component.literal("[§d§lKeychanger§r] Set user to: §l" + player.getName().getString()), false);
        } catch (CommandSyntaxException e) {
            sender.sendSystemMessage(Component.literal(e.getMessage()));
        }


        return 1;
    }

    static int reset_user_command(CommandContext<CommandSourceStack> commandContext) {
        KeychangerMod.User = null;
        commandContext.getSource().getServer().getPlayerList().broadcastSystemMessage(Component.literal("[§d§lKeychanger§r] Unset User"), false);

        return 1;
    }
}
