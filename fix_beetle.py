import os
path = 'C:/Users/jorge/IdeaProjects/proyecto_intento/src/main/java/org/examplee/proyecto_intento/entity/DungBeetleEntity.java'
with open(path, 'r', encoding='utf-8') as f:
    content = f.read()

imports = '''
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import net.minecraft.village.TradedItem;
'''
content = content.replace('import net.minecraft.block.Blocks;', 'import net.minecraft.block.Blocks;\\n' + imports)
content = content.replace(
    'public final class DungBeetleEntity extends AnimalEntity {',
    'public final class DungBeetleEntity extends AnimalEntity implements Merchant {'
)
fields = '''
    @Nullable
    private PlayerEntity customer;
    @Nullable
    private TradeOfferList offers;
'''
content = content.replace('private boolean nurseryResident;', 'private boolean nurseryResident;\\n' + fields)

methods = '''
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        
        if (itemStack.isOf(Items.NAME_TAG) || this.isBreedingItem(itemStack)) {
            ActionResult result = super.interactMob(player, hand);
            if (result.isAccepted()) return result;
            if (this.isBreedingItem(itemStack)) return result;
        }
        
        if (this.isTrader() && this.isAlive() && !this.hasCustomer() && !this.isBaby()) {
            if (hand == Hand.MAIN_HAND) {
                // optional
            }
            if (!this.getOffers().isEmpty()) {
                if (!this.getWorld().isClient) {
                    this.setCustomer(player);
                    this.sendOffers(player, this.getDisplayName(), 1);
                }
                return ActionResult.success(this.getWorld().isClient);
            }
        }
        
        return super.interactMob(player, hand);
    }
    
    public boolean hasCustomer() {
        return this.customer != null;
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        this.customer = customer;
    }

    @Override
    @Nullable
    public PlayerEntity getCustomer() {
        return this.customer;
    }

    @Override
    public TradeOfferList getOffers() {
        if (this.offers == null) {
            this.offers = new TradeOfferList();
            
            // Check TradedItem in 1.21.1:
            // public TradedItem(ItemConvertible item, int count) or something like that.
            // Wait, we can construct TradedItem using Item provider maybe?
            // Let's bet on new TradedItem(ItemConvertible item, int count) or TradedItem(Item) 
            // In 1.21 it is 
ew TradedItem(Item). Wait.
            
            // Wait, actually earlier I saw 
ew TradedItem(Items.EMERALD, 1). Let's just try.
            this.offers.add(new TradeOffer(new TradedItem(Items.EMERALD, 1), new ItemStack(ModItems.POPO, 4), 10, 5, 0.05f));
            this.offers.add(new TradeOffer(new TradedItem(ModItems.POPO, 10), new ItemStack(Items.EMERALD, 1), 10, 5, 0.05f));
            this.offers.add(new TradeOffer(new TradedItem(Items.EMERALD, 3), new ItemStack(ModItems.FERTILIZER, 1), 10, 5, 0.05f));
        }
        return this.offers;
    }

    @Override
    public void setOffersFromServer(@Nullable TradeOfferList offers) {
        this.offers = offers;
    }

    @Override
    public void trade(TradeOffer offer) {
        offer.use();
        this.ambientSoundChance = -this.getMinAmbientSoundDelay();
        this.onSellingItem(offer.getSellItem());
    }

    @Override
    public void onSellingItem(ItemStack stack) {
        if (!this.getWorld().isClient && this.ambientSoundChance > -this.getMinAmbientSoundDelay() + 20) {
            this.ambientSoundChance = -this.getMinAmbientSoundDelay();
            this.playSound(this.getYesSound(), this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperienceFromServer(int experience) {
    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public SoundEvent getYesSound() {
        return ModSounds.BEETLE_CHIRP;
    }

    @Override
    public boolean isClient() {
        return this.getWorld().isClient;
    }
'''
p = content.rfind('}')
content = content[:p] + methods + content[p:]
with open(path, 'w', encoding='utf-8') as f:
    f.write(content)
