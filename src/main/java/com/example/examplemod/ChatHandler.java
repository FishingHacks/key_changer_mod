package com.example.examplemod;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.example.examplemod.KeychangerMod.LOGGER;

public class ChatHandler {
    @SubscribeEvent
    public static void onChatReceive(ClientChatReceivedEvent ev) {
        if (ev.isSystem()) {
            String contents = ev.getMessage().getString();
            // command to change key: `key_changer:mapping_name:new_key`
            if (contents.startsWith("key_changer:")) {
                ev.setCanceled(true);
                String[] strings = contents.substring(12).split(":", 2);
                if (strings.length == 2) {
                    String key_name = strings[0];
                    String key_identifier = strings[1];
                    LOGGER.info("Change {} to {}", key_name, key_identifier);
                    for (KeyMapping mapping : Minecraft.getInstance().options.keyMappings) {
                        if (mapping.getName().equals(key_name)) {
                            LOGGER.info("Setting Keymapping {} to {}", mapping.getName(), key_identifier);
                            try {
                                InputConstants.Key new_key = InputConstants.getKey(key_identifier);
                                mapping.setKey(new_key);
                                Minecraft.getInstance().options.setKey(mapping, new_key);
                                Minecraft.getInstance().options.save();
                                KeyMapping.resetMapping();
                            } catch (IllegalArgumentException e) {
                                LOGGER.info("Could not find key {}", key_identifier);
                            }
                        }
                    }
                }
            }
        }
    }
}
