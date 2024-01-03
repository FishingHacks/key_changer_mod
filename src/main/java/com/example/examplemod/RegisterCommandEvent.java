package com.example.examplemod;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegisterCommandEvent {
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        ChangeKeybindCommand.register(event.getDispatcher());
        ChangeUserCommand.register(event.getDispatcher());
        HelpCommand.register(event.getDispatcher());
        HelpCommand.init();
    }
}
