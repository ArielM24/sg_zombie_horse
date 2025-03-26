package com.sg.zombie_horse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sg.zombie_horse.IEntityDataSaver;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

@Mixin(Entity.class)
public abstract class DataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;

    @Override
    public NbtCompound getZombieHorsePersistentData(){
        if(this.persistentData == null){
            this.persistentData = new NbtCompound();
        }
        return this.persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info){
        if(persistentData != null){
            nbt.put("sg_zombie_horsepersistent_data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info){
        if(nbt.contains("sg_zombie_horsepersistent_data")){
            persistentData = nbt.getCompound("sg_zombie_horsepersistent_data").get();
        }
    }
}
