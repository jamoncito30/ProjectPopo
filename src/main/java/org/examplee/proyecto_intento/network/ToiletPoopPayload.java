package org.examplee.proyecto_intento.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ToiletPoopPayload() implements CustomPayload {
    public static final CustomPayload.Id<ToiletPoopPayload> ID = new CustomPayload.Id<>(Identifier.of("proyecto_intento", "toilet_poop"));
    public static final PacketCodec<RegistryByteBuf, ToiletPoopPayload> CODEC = PacketCodec.unit(new ToiletPoopPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
