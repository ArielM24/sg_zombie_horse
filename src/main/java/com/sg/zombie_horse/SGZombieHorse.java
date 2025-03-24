package com.sg.zombie_horse;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Difficulty;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SGZombieHorse implements ModInitializer {
	public static final String MOD_ID = "sg-zombie-horse";

	public static final Random r = new Random();

	public static final int spawnRatio = 0;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		
		ServerEntityEvents.ENTITY_LOAD.register((entity, serverLevel) -> {
			if(!entity.getWorld().getDifficulty().equals(Difficulty.HARD)){
				return;
			}
			if (!entity.getType().equals(EntityType.ZOMBIE)) {
				return;
			}
			ZombieEntity zombie = (ZombieEntity) entity;
			NbtElement check = ((IEntityDataSaver)zombie).getPersistentData().get("zombie_horse");
			if(check != null){
				return;
			}
			((IEntityDataSaver)zombie).getPersistentData().putString("zombie_horse", "checked");
			boolean willSpawn = r.nextInt(100) <= spawnRatio;
			if (!willSpawn) {
				return;
			}
			if (zombie.getPos().getY() < 64) {
				return;
			}
			BlockPos pos = zombie.getBlockPos();
			if(!zombie.getWorld().getBlockState(pos.offset(Direction.UP, 2)).isAir()){
				return;
			};
			ZombieHorseEntity zombieHorse = EntityType.ZOMBIE_HORSE.create(serverLevel, SpawnReason.NATURAL);
			zombieHorse.setPos(zombie.getPos().getX(), zombie.getPos().getY(), zombie.getPos().getZ());
			zombieHorse.saddle(new ItemStack(Items.SADDLE), null);
			zombieHorse.setTame(true);
			if(zombie.isBaby()){
				zombieHorse.setBaby(true);
			}
			serverLevel.spawnNewEntityAndPassengers(zombieHorse);
			zombie.startRiding(zombieHorse);
		});
	}
}