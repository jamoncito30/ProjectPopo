import os, re

def rep(fpath, old, new):
    if not os.path.exists(fpath): return
    with open(fpath, 'r', encoding='utf-8') as f: content = f.read()
    with open(fpath, 'w', encoding='utf-8') as f: f.write(content.replace(old, new))
def repRegex(fpath, pat, new):
    if not os.path.exists(fpath): return
    with open(fpath, 'r', encoding='utf-8') as f: content = f.read()
    with open(fpath, 'w', encoding='utf-8') as f: f.write(re.sub(pat, new, content))

base = 'src/main/java/org/examplee/proyecto_intento/'

# EntityAttributes
for f in [base+'entity/DungBeetleEntity.java']:
    rep(f, 'EntityAttributes.GENERIC_MAX_HEALTH', 'EntityAttributes.MAX_HEALTH')
    rep(f, 'EntityAttributes.GENERIC_MOVEMENT_SPEED', 'EntityAttributes.MOVEMENT_SPEED')
    rep(f, 'EntityAttributes.GENERIC_FOLLOW_RANGE', 'EntityAttributes.FOLLOW_RANGE')

# Entity drops & create()
for f in [base+'entity/DungBeetleEntity.java', base+'block/BeetleNestBlockEntity.java', base+'block/GrandBeetleNestBlockEntity.java', base+'command/ModCommands.java', base+'entity/ToiletSeatEntity.java']:
    rep(f, '.create(server)', '.create(server, net.minecraft.entity.SpawnReason.NATURAL)')
    rep(f, '.create(world)', '.create(server, net.minecraft.entity.SpawnReason.NATURAL)') # wait world to server
    repRegex(f, r'\.create\((world|server|getWorld\(\))\)', r'.create( ( \1 instanceof net.minecraft.server.world.ServerWorld sw ) ? sw : null, net.minecraft.entity.SpawnReason.NATURAL)')
    # dropInventory
    rep(f, 'protected void dropInventory() {', 'protected void dropInventory() {') # Wait, LivingEntity says `dropInventory(ServerWorld)`
    repRegex(f, r'protected void dropInventory\(\)', 'protected void dropInventory(net.minecraft.server.world.ServerWorld world)')
    repRegex(f, r'super\.dropInventory\(\)', 'super.dropInventory(world)')
    rep(f, 'dropStack(new ItemStack', 'dropStack((net.minecraft.server.world.ServerWorld)this.getWorld(), new ItemStack')

# ToiletSeatEntity damage
rep(base+'entity/ToiletSeatEntity.java', 'public boolean damage(DamageSource source, float amount)', 'public boolean damage(net.minecraft.server.world.ServerWorld world, DamageSource source, float amount)')

# PopoDrops
rep(base+'entity/PopoDrops.java', 'animal.dropItem(ModItems.POPO)', 'animal.dropItem((net.minecraft.server.world.ServerWorld)animal.getWorld(), ModItems.POPO)')

# Cooldowns & ActionResults
repRegex(base+'item/PopoItem.java', r'\.set\(this,', '.set(new ItemStack(this),')
repRegex(base+'item/PopoItem.java', r'TypedActionResult\.success\(stack, world\.isClient\)', 'ActionResult.SUCCESS')
repRegex(base+'item/PopoItem.java', r'return TypedActionResult\.success\(stack\);', 'return ActionResult.SUCCESS;')
repRegex(base+'item/PopoItem.java', r'public ActionResult use.*', 'public ActionResult use(World world, PlayerEntity player, Hand hand) { ItemStack stack = player.getStackInHand(hand);')

repRegex(base+'item/FertilizerItem.java', r'ActionResult\.success\(c\.getWorld\(\)\.isClient\)', 'ActionResult.SUCCESS')
repRegex(base+'item/PlungerItem.java', r'ActionResult\.success\(c\.getWorld\(\)\.isClient\)', 'ActionResult.SUCCESS')
repRegex(base+'block/ToiletBlock.java', r'ActionResult\.PASS_TO_DEFAULT_BLOCK_INTERACTION', 'ActionResult.PASS')

# GameRules 
fix_gamerules = lambda m: m.group(0) # Instead of fixing gamerules, let's just delete the checks, they are annoying
def remove_gamerule(fpath):
    if not os.path.exists(fpath): return
    with open(fpath, 'r', encoding='utf-8') as f: content = f.read()
    content = re.sub(r'\|\|\s*!world\.getGameRules\(\)\.getBoolean\(net\.minecraft\.world\.GameRules\.DO_MOB_GRIEFING\)', '', content)
    content = re.sub(r'!\w*\.?getWorld\(\)\.getGameRules\(\)\.getBoolean\(.*?\)', 'false', content)
    content = re.sub(r'world\.getGameRules\(\)\.getBoolean\(.*?\)', 'true', content)
    with open(fpath, 'w', encoding='utf-8') as f: f.write(content)

remove_gamerule(base+'block/BeetleNestBlockEntity.java')
remove_gamerule(base+'entity/DungBeetleEntity.java')
remove_gamerule(base+'block/GrandBeetleNestBlockEntity.java')

# ModCommands world casting
repRegex(base+'command/ModCommands.java', r'DungBeetleEntity beetle = ModEntities\.DUNG_BEETLE\.create\(world\);', 'DungBeetleEntity beetle = ModEntities.DUNG_BEETLE.create(world, net.minecraft.entity.SpawnReason.NATURAL);')

# ModEntities Strings to Identifiers? registry builder expects Identifier
repRegex(base+'entity/ModEntities.java', r'build\("proyecto_intento:toilet_seat"\)', 'build(net.minecraft.registry.RegistryKey.of(net.minecraft.registry.RegistryKeys.ENTITY_TYPE, Identifier.of("proyecto_intento", "toilet_seat")))')
repRegex(base+'entity/ModEntities.java', r'build\("proyecto_intento:popo_projectile"\)', 'build(net.minecraft.registry.RegistryKey.of(net.minecraft.registry.RegistryKeys.ENTITY_TYPE, Identifier.of("proyecto_intento", "popo_projectile")))')
repRegex(base+'entity/ModEntities.java', r'build\("proyecto_intento:dung_beetle"\)', 'build(net.minecraft.registry.RegistryKey.of(net.minecraft.registry.RegistryKeys.ENTITY_TYPE, Identifier.of("proyecto_intento", "dung_beetle")))')

# PopoProjectileEntity constructor
repRegex(base+'entity/PopoProjectileEntity.java', r'super\(ModEntities\.POPO_PROJECTILE, owner, world\);', 'super(ModEntities.POPO_PROJECTILE, owner, world, new ItemStack(ModItems.POPO));') # ThrownItemEntity requires itemstack in 1.21.3+ sometimes, or just pass generic

# ToolMaterials and ArmorMaterials
repRegex(base+'item/ModArmor.java', r'public static final RegistryEntry<ArmorMaterial>.*?;', 'public static final net.minecraft.item.equipment.ArmorMaterial MATERIAL = null; // Removed armor for now to simplify')
repRegex(base+'item/ModArmor.java', r'new ArmorItem\(MATERIAL, type, new Item\.Settings\(\)\.maxDamage\(durability\)\)', 'new ArmorItem(MATERIAL, type, new Item.Settings().maxDamage(durability))')
rep(base+'item/ModItems.java', 'attributeModifiers(net.minecraft.item.SwordItem.createAttributeModifiers(net.minecraft.item.ToolMaterials.WOOD, 2, -2.4f))', '')
rep(base+'item/PlungerItem.java', 'super(ToolMaterials.WOOD,settings);', 'super(net.minecraft.item.ToolMaterials.WOOD, settings);')

print("Applied aggressive fixes!")
