package org.examplee.proyecto_intento;

import net.fabricmc.api.ModInitializer;
import org.examplee.proyecto_intento.item.ModItems;
import org.examplee.proyecto_intento.item.ModArmor;
import org.examplee.proyecto_intento.block.ModBlocks;
import org.examplee.proyecto_intento.item.ModItemGroups;
import org.examplee.proyecto_intento.entity.ModEntities;
import org.examplee.proyecto_intento.entity.ModEffects;
import org.examplee.proyecto_intento.entity.ModParticles;
import org.examplee.proyecto_intento.entity.ModSounds;
import org.examplee.proyecto_intento.entity.SmellSystem;
import org.examplee.proyecto_intento.command.ModCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Proyecto_intento implements ModInitializer {

    public static final String MOD_ID = "proyecto_intento";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("¡Inicializando PopoCraft (proyecto_intento)!");
        
        // Registramos todos los ítems
        ModItems.registerModItems();
        ModBlocks.initialize();
        ModArmor.initialize();
        ModEntities.initialize();
        ModEffects.initialize();
        org.examplee.proyecto_intento.item.ModPotions.initialize();
        ModParticles.initialize();
        ModSounds.initialize();
        ModItemGroups.initialize();
        SmellSystem.initialize();
        org.examplee.proyecto_intento.entity.InvasionSystem.initialize();
        org.examplee.proyecto_intento.entity.DigestionSystem.initialize();
        org.examplee.proyecto_intento.network.ModNetworking.register();
        ModCommands.register();
    }
}
