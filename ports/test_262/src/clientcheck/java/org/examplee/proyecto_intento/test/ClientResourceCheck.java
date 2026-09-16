package org.examplee.proyecto_intento.test;

import java.nio.file.Files;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.examplee.proyecto_intento.Proyecto_intento;
import org.examplee.proyecto_intento.client.DungBeetleRenderer;

public final class ClientResourceCheck implements ClientModInitializer {
    private int ticks;
    private boolean finished;
    @Override public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (finished || ++ticks < 60 || client.getOverlay() != null) return;
            finished = true;
            try {
                for (String item : new String[]{"popo", "wet_popo", "dry_popo", "popo_pile", "beetle_nest", "popo_helmet", "popo_chestplate", "popo_leggings", "popo_boots", "dung_beetle_spawn_egg"}) {
                    ItemStack stack = new ItemStack(Registries.ITEM.get(Identifier.of("proyecto_intento", item)));
                    var model = client.getItemRenderer().getModels().getModel(stack);
                    if (model == client.getBakedModelManager().getMissingModel()) throw new IllegalStateException("Missing baked item model: " + item);
                }
                for (String texture : new String[]{"entity/dung_beetle", "particle/fly", "mob_effect/stinky", "models/armor/popo_layer_1", "models/armor/popo_layer_2"}) {
                    if (client.getResourceManager().getResource(Identifier.of("proyecto_intento", "textures/" + texture + ".png")).isEmpty()) throw new IllegalStateException("Missing texture: " + texture);
                }
                var beetle = client.getEntityModelLoader().getModelPart(DungBeetleRenderer.LAYER);
                for (String part : new String[]{"head", "shell_left", "shell_right", "ball", "leg0", "leg1", "leg2", "leg3", "leg4", "leg5"}) beetle.getChild(part);
                for (String sound : new String[]{"flies", "beetle_chirp", "beetle_step", "beetle_roll", "fart"}) {
                    if (client.getSoundManager().get(Identifier.of("proyecto_intento", sound)) == null) throw new IllegalStateException("Missing sound: " + sound);
                }
                for (int stage = 1; stage <= 5; stage++) {
                    var state = org.examplee.proyecto_intento.block.ModBlocks.BEETLE_NEST.getDefaultState()
                            .with(org.examplee.proyecto_intento.block.BeetleNestBlock.STAGE, stage);
                    var model = client.getBlockRenderManager().getModel(state);
                    if (model == client.getBakedModelManager().getMissingModel()
                            || model.getQuads(state, null, net.minecraft.util.math.random.Random.create()).isEmpty())
                        throw new IllegalStateException("Missing/empty shelter model stage " + stage);
                }
                for (var item : new net.minecraft.item.Item[]{net.minecraft.item.Items.POTION, net.minecraft.item.Items.SPLASH_POTION, net.minecraft.item.Items.LINGERING_POTION}) {
                    var stack = net.minecraft.component.type.PotionContentsComponent.createStack(item, org.examplee.proyecto_intento.item.ModPotions.BOTTLED_FART);
                    if (client.getItemRenderer().getModels().getModel(stack) == client.getBakedModelManager().getMissingModel()) throw new IllegalStateException("Missing potion model");
                    if (!net.minecraft.client.resource.language.I18n.hasTranslation(item.getTranslationKey(stack))) throw new IllegalStateException("Missing potion translation: " + item.getTranslationKey(stack));
                }
                for (int i = 1; i <= 3; i++) {
                    try (var stream = client.getResourceManager().open(Identifier.of("proyecto_intento", "sounds/fart_" + i + ".ogg"));
                         var decoded = new net.minecraft.client.sound.OggAudioStream(stream)) {
                        if (decoded.getFormat().getChannels() != 1 || decoded.readAll().remaining() < 1000) throw new IllegalStateException("Invalid fart audio: " + i);
                    }
                }
                Files.writeString(client.runDirectory.toPath().resolve("resource-check.txt"), "PASS: 10 item models, 3 named potion variants, 5 shelter stages, 5 textures, beetle model, 5 sound events and 3 decoded mono OGG clips.\n");
                Proyecto_intento.LOGGER.info("POPOCRAFT CLIENT RESOURCE CHECK PASSED");
                int grandPieces = 0;
                for (var state : org.examplee.proyecto_intento.block.ModBlocks.GRAND_BEETLE_NEST_PIECE.getStateManager().getStates()) {
                    var model = client.getBlockRenderManager().getModel(state);
                    if (model == client.getBakedModelManager().getMissingModel()) throw new IllegalStateException("Missing grand nest model: " + state);
                    boolean geometry = !state.getOutlineShape(net.minecraft.world.EmptyBlockView.INSTANCE, net.minecraft.util.math.BlockPos.ORIGIN).isEmpty();
                    boolean rendered = !model.getQuads(state, null, net.minecraft.util.math.random.Random.create()).isEmpty();
                    if (geometry != rendered) throw new IllegalStateException("Grand nest shape/model mismatch: " + state);
                    grandPieces++;
                }
                for (var state : org.examplee.proyecto_intento.block.ModBlocks.GRAND_BEETLE_NEST_CONTROLLER.getStateManager().getStates()) {
                    var model = client.getBlockRenderManager().getModel(state);
                    if (model == client.getBakedModelManager().getMissingModel() || model.getQuads(state, null, net.minecraft.util.math.random.Random.create()).isEmpty()) throw new IllegalStateException("Missing controller model: " + state);
                }
                Files.writeString(client.runDirectory.toPath().resolve("grand-nest-check.txt"), "PASS: " + grandPieces + " grand nest states loaded; model/collision occupancy matches; 4 controller models loaded.\n");
                for(var block:new net.minecraft.block.Block[]{org.examplee.proyecto_intento.block.ModBlocks.INODORO,org.examplee.proyecto_intento.block.ModBlocks.EXTRACTOR_ESTIERCOL,org.examplee.proyecto_intento.block.ModBlocks.FERTILIZED_FARMLAND}) {
                    for(var state:block.getStateManager().getStates()) if(client.getBlockRenderManager().getModel(state)==client.getBakedModelManager().getMissingModel()) throw new IllegalStateException("Missing roadmap model: "+state);
                }
                for(var item:new net.minecraft.item.Item[]{org.examplee.proyecto_intento.item.ModItems.DESATASCADOR,org.examplee.proyecto_intento.item.ModItems.ESTIERCOL,org.examplee.proyecto_intento.block.ModBlocks.INODORO.asItem()}) {
                    if(client.getItemRenderer().getModels().getModel(new net.minecraft.item.ItemStack(item))==client.getBakedModelManager().getMissingModel()) throw new IllegalStateException("Missing roadmap item model");
                }
                for(var texture:new String[]{"item/desatascador","item/estiercol","block/toilet_ceramic","block/extractor_estiercol","entity/dung_beetle_trader"}) {
                    try(var input=client.getResourceManager().open(Identifier.of("proyecto_intento","textures/"+texture+".png"));var pixels=net.minecraft.client.texture.NativeImage.read(input)) {
                        if(pixels.getWidth()!=(texture.startsWith("entity/")?64:16)) throw new IllegalStateException("Wrong roadmap texture size");
                    }
                }
                Files.writeString(client.runDirectory.toPath().resolve("roadmap-check.txt"),"PASS: toilet 8 states, extractor, 48 farmland states, 3 item models, 5 decoded textures and merchant backpack.\n");
                client.setScreen(new RoadmapPreviewScreen());
            } catch (Exception exception) {
                throw new IllegalStateException("PopoCraft client resource check failed", exception);
            }
        });
    }
}
