import sys
import re

with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'r') as f:
    text = f.read()

# Make sure they have a "IsTrader" boolean and check for drop shell on grow
nbt_methods = '''    @Override
    public void writeCustomDataToNbt(net.minecraft.nbt.NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsTrader", this.dataTracker.get(IS_TRADER));
        nbt.putBoolean("HasDroppedShell", this.hasDroppedShell);
    }

    @Override
    public void readCustomDataFromNbt(net.minecraft.nbt.NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(IS_TRADER, nbt.getBoolean("IsTrader"));
        this.hasDroppedShell = nbt.getBoolean("HasDroppedShell");
    }
'''

# We need to add standard data trackers
data_trackers = '''    private static final net.minecraft.entity.data.TrackedData<Boolean> IS_TRADER = net.minecraft.entity.data.DataTracker.registerData(DungBeetleEntity.class, net.minecraft.entity.data.TrackedDataHandlerRegistry.BOOLEAN);
    private boolean hasDroppedShell = false;

    @Override
    protected void initDataTracker(net.minecraft.entity.data.DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_TRADER, false);
    }
'''

if "IS_TRADER" not in text:
    # insert data trackers after public class
    text = text.replace("public class DungBeetleEntity extends AnimalEntity {", "public class DungBeetleEntity extends AnimalEntity {\n" + data_trackers)
    
    # insert nbt read/write if they don't have it
    if "writeCustomDataToNbt" not in text:
        text = text.replace("    public boolean isInvulnerableTo(DamageSource source) {", nbt_methods + "\n    @Override\n    public boolean isInvulnerableTo(DamageSource source) {")
    else:
        text = text.replace("super.writeCustomDataToNbt(nbt);", "super.writeCustomDataToNbt(nbt);\n        nbt.putBoolean(\"IsTrader\", this.dataTracker.get(IS_TRADER));\n        nbt.putBoolean(\"HasDroppedShell\", this.hasDroppedShell);")
        text = text.replace("super.readCustomDataFromNbt(nbt);", "super.readCustomDataFromNbt(nbt);\n        this.dataTracker.set(IS_TRADER, nbt.getBoolean(\"IsTrader\"));\n        this.hasDroppedShell = nbt.getBoolean(\"HasDroppedShell\");")
        
    old_tick = '''    public void tick() {
        super.tick();'''
        
    new_tick = '''    public void tick() {
        super.tick();
        
        if (!world.isClient() && !isBaby() && !hasDroppedShell) {
            hasDroppedShell = true;
            this.dropItem(org.examplee.proyecto_intento.item.ModItems.BEETLE_SHELL);
            ((net.minecraft.server.world.ServerWorld)world).spawnParticles(net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER, getX(), getY()+0.5, getZ(), 5, 0.2, 0.2, 0.2, 0);
            
            // 5% chance to become trader upon growing up
            if (random.nextFloat() < 0.05f) {
                this.dataTracker.set(IS_TRADER, true);
            }
        }
'''
    text = text.replace(old_tick, new_tick)
    
    with open('src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java', 'w') as f:
        f.write(text)
    print("DungBeetle logic updated")

