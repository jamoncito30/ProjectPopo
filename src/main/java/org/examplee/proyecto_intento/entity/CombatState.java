package org.examplee.proyecto_intento.entity;

import java.util.UUID;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

/** Identity is persisted separately from the client-visible equipment role. */
public final class CombatState {
    public UUID event;
    public BlockPos nest;
    public int cooldown, repath;
    public double originalHealth = 8;
    public void write(NbtCompound root,int role) {
        if(event==null || nest==null)return;
        var n=new NbtCompound();n.putUuid("Event",event);n.putLong("Nest",nest.asLong());
        n.putInt("Role",role);n.putDouble("OriginalMaxHealth",originalHealth);root.put("Invasion",n);
    }
    public int read(NbtCompound root) {
        var n=root.getCompound("Invasion");
        event=n.containsUuid("Event")?n.getUuid("Event"):null;
        nest=event==null?null:BlockPos.fromLong(n.getLong("Nest"));
        originalHealth=n.contains("OriginalMaxHealth")?n.getDouble("OriginalMaxHealth"):8;
        return event==null?0:Math.clamp(n.getInt("Role"),0,3);
    }
    public void clear(){event=null;nest=null;cooldown=0;repath=0;}
}
