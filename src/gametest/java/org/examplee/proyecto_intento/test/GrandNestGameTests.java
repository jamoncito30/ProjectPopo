package org.examplee.proyecto_intento.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.examplee.proyecto_intento.block.*;
import org.examplee.proyecto_intento.item.ModItems;

public final class GrandNestGameTests implements FabricGameTest {
    @GameTest(templateName="popocraft_tests:arena")
    public void deconstructionConservesFoodAndLayers(TestContext c) {
        BlockPos p=new BlockPos(12,3,12);
        c.setBlockState(p,ModBlocks.BEETLE_NEST.getDefaultState().with(BeetleNestBlock.STAGE,5));
        var small=(BeetleNestBlockEntity)c.getWorld().getBlockEntity(c.getAbsolutePos(p));
        for(int i=0;i<8;i++) small.addMaterial();
        for(int i=0;i<8;i++) c.assertTrue(small.takeMaterialForGrandNest(),"Stored food recovered once");
        c.assertEquals(small.getFood(),0,"Store emptied");
        c.assertEquals(small.getCachedState().get(BeetleNestBlock.STAGE),5,"Food withdrawn before structural layers");
        for(int i=0;i<5;i++) c.assertTrue(small.takeMaterialForGrandNest(),"One layer recovered per trip");
        c.assertTrue(c.getWorld().getBlockState(c.getAbsolutePos(p)).isAir(),"Last layer removes shelter");
        c.assertTrue(c.getEntities(EntityType.ITEM).isEmpty(),"No duplicated loose food while recovering materials");c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void obstructedConstructionDoesNotDestroyPlayerBlocks(TestContext c) {
        var nest=place(c,1);var origin=nest.getOrigin();
        c.getWorld().setBlockState(origin.up(2),Blocks.DIAMOND_BLOCK.getDefaultState());
        var offered=new ItemStack(ModItems.POPO,64);
        c.assertEquals(nest.deposit(offered),0,"Obstruction refuses construction material");
        nest.updateStructure(4);
        c.assertTrue(c.getWorld().getBlockState(origin.up(2)).isOf(Blocks.DIAMOND_BLOCK),"Player block preserved");
        c.assertEquals(offered.getCount(),64,"Unspent material stays with carrier");c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void communalCapacityAndBreakingPreserveIdentities(TestContext c) {
        var nest=place(c,4);var abs=c.getAbsolutePos(POS);
        java.util.List<java.util.UUID> ids=new java.util.ArrayList<>();
        for(int i=0;i<30;i++) {
            var beetle=org.examplee.proyecto_intento.entity.ModEntities.DUNG_BEETLE.create(c.getWorld());
            beetle.setPosition(abs.toCenterPos());beetle.setAiDisabled(true);c.getWorld().spawnEntity(beetle);ids.add(beetle.getUuid());
            c.assertTrue(nest.tryEnter(beetle),"Thirty residents enter");
        }
        var extra=org.examplee.proyecto_intento.entity.ModEntities.DUNG_BEETLE.create(c.getWorld());extra.setPosition(abs.toCenterPos());
        c.getWorld().spawnEntity(extra);ids.add(extra.getUuid());
        c.assertTrue(nest.tryEnter(extra),"Admission no longer capped at thirty residents");
        c.waitAndRun(2,()->{
            c.setBlockState(POS,Blocks.AIR);
            c.waitAndRun(2,()->{
                for(var id:ids) c.assertTrue(c.getWorld().getEntity(id)!=null && c.getWorld().getEntity(id).isAlive(),"Every original UUID survives controller break and subsequent ticks");
                c.assertEquals(nest.getOccupantCount(),0,"No serialized duplicate residents left");c.complete();
            });
        });
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void flatSiteAndTenBlockSpacing(TestContext c) {
        for(int x=1;x<31;x++) for(int z=1;z<31;z++) c.setBlockState(x,1,z,Blocks.STONE);
        BlockPos first=c.getAbsolutePos(new BlockPos(2,2,2));
        c.getWorld().setBlockState(first.add(2,0,2),ModBlocks.GRAND_BEETLE_NEST_CONTROLLER.getDefaultState());
        var nest=(GrandBeetleNestBlockEntity)c.getWorld().getBlockEntity(first.add(2,0,2));nest.setOrigin(first);
        c.assertFalse(GrandBeetleNestBlockEntity.isValidSite(c.getWorld(),first.add(14,0,0)),"Nine empty blocks between edges is insufficient");
        c.assertTrue(GrandBeetleNestBlockEntity.isValidSite(c.getWorld(),first.add(15,0,0)),"Ten empty blocks between edges is valid");
        c.getWorld().setBlockState(first.add(15,-1,0),Blocks.AIR.getDefaultState());
        c.assertFalse(GrandBeetleNestBlockEntity.isValidSite(c.getWorld(),first.add(15,0,0)),"Hole in foundation rejects terrain");c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void largeColonyAssignsFourBuildersToExpand(TestContext c) {
        BlockPos absoluteOrigin=c.getAbsolutePos(new BlockPos(6,3,6));
        for(int x=0;x<32;x++)for(int z=0;z<32;z++)c.getWorld().setBlockState(c.getAbsolutePos(new BlockPos(x,2,z)),Blocks.STONE.getDefaultState());
        var nest=place(c,4);nest.setOrigin(absoluteOrigin);nest.updateStructure(4);
        java.util.List<org.examplee.proyecto_intento.entity.DungBeetleEntity> beetles=new java.util.ArrayList<>();
        for(int i=0;i<19;i++) {
            var beetle=org.examplee.proyecto_intento.entity.ModEntities.DUNG_BEETLE.create(c.getWorld());
            beetle.setNest(nest.getPos());beetle.setPosition(absoluteOrigin.getX()+10+(i%5),absoluteOrigin.getY(),absoluteOrigin.getZ()+10+(i/5));
            c.getWorld().spawnEntity(beetle);beetles.add(beetle);
        }
        c.assertFalse(nest.tryStartGrandNestExpansion(c.getWorld()),"Nineteen assigned beetles do not trigger a new grand nest");
        var twentieth=org.examplee.proyecto_intento.entity.ModEntities.DUNG_BEETLE.create(c.getWorld());
        twentieth.setNest(nest.getPos());twentieth.setPosition(absoluteOrigin.getX()+14,absoluteOrigin.getY(),absoluteOrigin.getZ()+14);
        c.getWorld().spawnEntity(twentieth);beetles.add(twentieth);
        c.assertTrue(nest.tryStartGrandNestExpansion(c.getWorld()),"Twenty assigned beetles trigger a repeatable expansion");
        c.assertTrue(nest.getExpansionTarget()!=null,"The source nest persists the new construction target");
        c.assertEquals((int)beetles.stream().filter(org.examplee.proyecto_intento.entity.DungBeetleEntity::isAssignedToGrandNestConstruction).count(),4,"Exactly four adults are randomly assigned as builders");
        c.assertTrue(c.getWorld().getBlockEntity(nest.getExpansionTarget()) instanceof GrandBeetleNestBlockEntity,"A new large nest starts construction at a validated site");
        c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void unassignedEmptySmallHouseReturnsAllMaterialsToGrandNest(TestContext c) {
        var main=place(c,4);
        BlockPos smallPos=c.getAbsolutePos(new BlockPos(12,3,12));
        c.getWorld().setBlockState(smallPos,ModBlocks.BEETLE_NEST.getDefaultState().with(BeetleNestBlock.STAGE,5));
        var small=(BeetleNestBlockEntity)c.getWorld().getBlockEntity(smallPos);
        var collector=org.examplee.proyecto_intento.entity.ModEntities.DUNG_BEETLE.create(c.getWorld());
        collector.setNest(main.getPos());collector.setPosition(smallPos.toCenterPos());c.getWorld().spawnEntity(collector);
        c.assertTrue(collector.isUnassignedEmptyHouse(smallPos,small),"Empty house with no assigned beetle can be reclaimed");
        java.util.UUID competingCollector=java.util.UUID.randomUUID();
        c.assertTrue(small.tryClaimForGrandNest(collector.getUuid()),"First collector reserves the house before travelling to it");
        c.assertFalse(small.tryClaimForGrandNest(competingCollector),"A competing collector cannot target the same house");
        collector.setNest(smallPos);
        c.assertFalse(collector.isUnassignedEmptyHouse(smallPos,small),"A house assigned to a live beetle is protected");
        small.releaseGrandNestClaim(collector.getUuid());
        c.assertTrue(small.tryClaimForGrandNest(competingCollector),"A released reservation can be claimed again");
        small.releaseGrandNestClaim(competingCollector);
        collector.setNest(main.getPos());
        for(int i=0;i<3;i++)c.assertTrue(small.addMaterial(),"Small-house food is stored");
        int recovered=0;
        for(int i=0;i<3;i++) {
            c.assertTrue(small.takeMaterialForGrandNest(),"Stored food is recovered once");
            recovered+=main.deposit(new ItemStack(ModItems.POPO,1));
        }
        for(int i=0;i<5;i++) {
            c.assertTrue(small.takeMaterialForGrandNest(),"Each empty construction layer is reclaimed once");
            recovered+=main.deposit(new ItemStack(ModItems.POPO,1));
        }
        c.assertTrue(c.getWorld().getBlockState(smallPos).isAir(),"Last recovered layer removes the abandoned house");
        c.assertEquals(recovered,8,"All stored food and five construction layers reach the grand-nest store");
        c.assertEquals(main.getFoodCount(),8,"Grand nest retains the full recovered amount");
        c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void fullColonyReusesVacantHouseBeforeFoundingAnother(TestContext c) {
        var main=place(c,4);
        BlockPos center=main.getPos();
        fillSmallHouseGround(c,center);
        BlockPos vacant=center.add(5,0,0);
        c.getWorld().setBlockState(vacant,ModBlocks.BEETLE_NEST.getDefaultState().with(BeetleNestBlock.STAGE,5));
        main.deposit(new ItemStack(ModItems.POPO,10));
        var releasedId=addSerializedResidents(c,main,30,null);

        c.assertTrue(main.tryFoundSmallNest(c.getWorld()),"A full colony uses its existing vacant house");
        var released=c.getWorld().getEntity(releasedId);
        c.assertTrue(released instanceof org.examplee.proyecto_intento.entity.DungBeetleEntity
                && vacant.equals(((org.examplee.proyecto_intento.entity.DungBeetleEntity)released).getNestPos()),
                "One stored resident is assigned to the existing vacant house");
        c.assertEquals(main.getOccupantCount(),29,"The assigned resident leaves the grand nest exactly once");
        c.assertEquals(main.getFoodCount(),10,"Reusing an existing house does not charge construction food");
        c.assertEquals(countSmallHouses(c,center),1,"A full colony does not create a replacement beside an empty house");
        c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void failedResidentReleaseDoesNotLeaveOrphanHouseOrSpendFood(TestContext c) {
        var main=place(c,4);
        BlockPos center=main.getPos();
        fillSmallHouseGround(c,center);
        main.deposit(new ItemStack(ModItems.POPO,10));
        var alreadyLoaded=org.examplee.proyecto_intento.entity.ModEntities.DUNG_BEETLE.create(c.getWorld());
        alreadyLoaded.setAiDisabled(true);
        alreadyLoaded.setPosition(center.add(-4,0,-4).toCenterPos());
        c.getWorld().spawnEntity(alreadyLoaded);
        addSerializedResidents(c,main,30,alreadyLoaded);

        c.assertFalse(main.tryFoundSmallNest(c.getWorld()),"Residents whose UUIDs are already active cannot be released again");
        c.assertEquals(countSmallHouses(c,center),0,"A resident that cannot be released does not leave a newly founded empty house");
        c.assertEquals(main.getOccupantCount(),30,"Failed release preserves all serialized residents");
        c.assertEquals(main.getFoodCount(),10,"Failed release preserves the founding cost");
        c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void fullColonyFoundsAndAssignsHouseWhenNoVacancyExists(TestContext c) {
        var main=place(c,4);
        BlockPos center=main.getPos();
        fillSmallHouseGround(c,center);
        main.deposit(new ItemStack(ModItems.POPO,10));
        var releasedId=addSerializedResidents(c,main,30,null);

        c.assertTrue(main.tryFoundSmallNest(c.getWorld()),"A spare resident founds a new house when no vacant house exists");
        c.assertEquals(main.getOccupantCount(),29,"The newly housed resident leaves the grand nest exactly once");
        c.assertEquals(main.getFoodCount(),4,"Six food are charged only after the resident is housed");
        c.assertEquals(countSmallHouses(c,center),1,"Exactly one new small house is founded");
        var released=c.getWorld().getEntity(releasedId);
        c.assertTrue(released instanceof org.examplee.proyecto_intento.entity.DungBeetleEntity
                && ((org.examplee.proyecto_intento.entity.DungBeetleEntity)released).getNestPos()!=null
                && c.getWorld().getBlockState(((org.examplee.proyecto_intento.entity.DungBeetleEntity)released).getNestPos()).isOf(ModBlocks.BEETLE_NEST),
                "The new house is assigned to its released resident");
        c.complete();
    }
    private static void fillSmallHouseGround(TestContext c,BlockPos center) {
        for(int x=-11;x<=11;x++)for(int z=-11;z<=11;z++)
            c.getWorld().setBlockState(center.add(x,-1,z),Blocks.STONE.getDefaultState());
    }
    private static java.util.UUID addSerializedResidents(TestContext c,GrandBeetleNestBlockEntity nest,int count,
            org.examplee.proyecto_intento.entity.DungBeetleEntity sharedEntity) {
        java.util.UUID lastId=null;
        for(int i=0;i<count;i++) {
            var beetle=sharedEntity==null?org.examplee.proyecto_intento.entity.ModEntities.DUNG_BEETLE.create(c.getWorld()):sharedEntity;
            NbtCompound data=new NbtCompound();
            beetle.saveNbt(data);
            lastId=data.getUuid("UUID");
            NbtCompound resident=new NbtCompound();
            resident.put("Entity",data);
            resident.putInt("TicksInside",0);
            nest.getOccupants().add(resident);
        }
        return lastId;
    }
    private static int countSmallHouses(TestContext c,BlockPos center) {
        int count=0;
        for(int x=-10;x<=10;x++)for(int y=-2;y<=2;y++)for(int z=-10;z<=10;z++)
            if(c.getWorld().getBlockState(center.add(x,y,z)).isOf(ModBlocks.BEETLE_NEST))count++;
        return count;
    }
    private static final BlockPos POS = new BlockPos(8,3,8);
    private static GrandBeetleNestBlockEntity place(TestContext c, int stage) {
        c.setBlockState(POS, ModBlocks.GRAND_BEETLE_NEST_CONTROLLER.getDefaultState().with(GrandBeetleNestPieceBlock.STAGE,stage));
        return (GrandBeetleNestBlockEntity)c.getWorld().getBlockEntity(c.getAbsolutePos(POS));
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void capacityPersistenceAndTransactionalDeposit(TestContext c) {
        var nest=place(c,4);
        int accepted=0, remaining=0;
        for(int i=0;i<6;i++) {
            var stack=new ItemStack(ModItems.POPO,64);
            accepted+=nest.deposit(stack); remaining+=stack.getCount();
        }
        c.assertEquals(accepted,360,"Accepts exactly 360");
        c.assertEquals(remaining,24,"Overflow stays with carrier");
        var overflow=new ItemStack(ModItems.POPO,1);
        c.assertEquals(nest.deposit(overflow),0,"Full store rejects further food");
        c.assertEquals(overflow.getCount(),1,"Rejected item is conserved");
        var stone=new ItemStack(Items.STONE,3);
        c.assertEquals(nest.deposit(stone),0,"Rejects non-poop");
        c.assertEquals(stone.getCount(),3,"Invalid input is untouched");
        var saved=nest.createNbt(c.getWorld().getRegistryManager());
        var loaded=new GrandBeetleNestBlockEntity(c.getAbsolutePos(POS),nest.getCachedState());
        loaded.read(saved,c.getWorld().getRegistryManager());
        c.assertEquals(loaded.getFoodCount(),360,"NBT round trip preserves food");
        c.assertFalse(nest.canInsert(0,overflow,Direction.UP),"Hoppers cannot feed store");
        c.complete();
    }
    @GameTest(templateName="popocraft_tests:arena",tickLimit=100)
    public void realHopperExtractsOnlyWhenComplete(TestContext c) {
        c.setBlockState(POS.down(),Blocks.HOPPER);
        var nest=place(c,3);
        nest.setStack(0,new ItemStack(ModItems.POPO,12));
        var hopper=(HopperBlockEntity)c.getWorld().getBlockEntity(c.getAbsolutePos(POS.down()));
        var offered=new ItemStack(ModItems.POPO);
        c.assertEquals(nest.deposit(offered), 1, "Construction receives popo for budget");
        c.waitAndRun(20,()->{
            c.assertTrue(hopper.isEmpty(),"Incomplete shelter blocks hopper extraction");
            var completed=place(c,4);
            c.assertEquals(completed.getFoodCount(),12,"Stage change preserves block entity inventory");
            c.waitAndRun(20,()->{
                int extracted=0;
                for(int i=0;i<hopper.size();i++) extracted+=hopper.getStack(i).getCount();
                c.assertTrue(extracted>0,"Actual lower hopper extracts poop");
                c.assertEquals(extracted+completed.getFoodCount(),12,"Hopper conserves total items");
                c.complete();
            });
        });
    }
    @GameTest(templateName="popocraft_tests:arena")
    public void breakingControllerReturnsAllStoredFood(TestContext c) {
        var nest=place(c,4);
        for(int i=0;i<6;i++) nest.deposit(new ItemStack(ModItems.POPO,64));
        c.setBlockState(POS,Blocks.AIR);
        int dropped=c.getEntities(EntityType.ITEM).stream().filter(e->e.getStack().isOf(ModItems.POPO)).mapToInt(e->e.getStack().getCount()).sum();
        c.assertEquals(dropped,360,"Breaking controller drops all stored food");
        c.assertEquals(nest.getFoodCount(),0,"Detached inventory is empty to prevent duplicate drops");
        c.complete();
    }
}
