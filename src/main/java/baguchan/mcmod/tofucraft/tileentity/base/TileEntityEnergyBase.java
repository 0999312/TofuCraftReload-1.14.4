package baguchan.mcmod.tofucraft.tileentity.base;

import baguchan.mcmod.tofucraft.api.tfenergy.ITofuEnergy;
import baguchan.mcmod.tofucraft.api.tfenergy.TofuNetwork;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class TileEntityEnergyBase extends TileEntity implements ITofuEnergy {

    /*
     * Comment:
     * This is the very basis of the Tofu Energy Storage Tile Entity (TESTE), which means that it's the lowest level of tile
     * which will be registered to the Tofu Energy Network instantiated in the TofuNetwork class.
     * It's highly configurable, and you can override most part of its function if you want.
     * */

    public static final String TAG_ENERGY = "tf_energy";
    public static final String TAG_ENERGY_MAX = "tf_energy_max";
    public static final String TAG_UUID = "tf_uuid";

    protected String uuid = "";

    protected int energy;
    protected int energyMax;

    public TileEntityEnergyBase(TileEntityType<?> tileEntityTypeIn, int energyMax) {
        super(tileEntityTypeIn);
        this.energyMax = energyMax;

    }

    //A public interface to access how much energy does a tile have
    @Override
    public int getEnergyStored() {
        return energy;
    }

    //Then how much it can store
    @Override
    public int getMaxEnergyStored() {
        return energyMax;
    }

    /*
     * It's used when you want to fill a certain amount of tf energy into the tile
     * To receive means how much energy will be filled this time, and if simulate is set to true, the code will just
     * simulate all the process.
     * Returns how much energy is filled.*/
    @Override
    public int receive(int toReceive, boolean simulate) {
        if (getEnergyStored() > getMaxEnergyStored()) return 0;
        int calculated = Math.min(toReceive, getMaxEnergyStored() - getEnergyStored());
        if (!simulate) energy += calculated;
        return calculated;
    }

    /*
     * Drains energy from the tile. Like what its counterpart above does.
     * */
    @Override
    public int drain(int toDrain, boolean simulate) {
        int calculated = Math.min(toDrain, getEnergyStored());
        if (!simulate) energy -= calculated;
        return calculated;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt(TAG_ENERGY, energy);
        compound.putString(TAG_UUID, uuid);
        compound.putInt(TAG_ENERGY_MAX, energyMax);
        return compound;
    }


    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.energyMax = compound.getInt(TAG_ENERGY_MAX);
        this.energy = compound.getInt(TAG_ENERGY);
        this.uuid = compound.getString(TAG_UUID);
    }

    /*
     * Check if the tile can receive/drain energy from the give tile
     * */
    @Override
    public boolean canReceive(TileEntity from) {
        if (!(from instanceof ITofuEnergy)) throw new IllegalArgumentException("It must be a instance of ITofuEnergy!");
        return true;
    }

    @Override
    public boolean canDrain(TileEntity to) {
        if (!(to instanceof ITofuEnergy)) throw new IllegalArgumentException("It must be a instance of ITofuEnergy!");
        return true;
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 99, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(pkt.getNbtCompound());
    }

    @Override
    public void validate() {
        super.validate();
        read(getUpdateTag());
        if (!world.isRemote) {
            if (uuid.isEmpty()) uuid = UUID.randomUUID().toString();
            TofuNetwork.Instance.register(uuid, this, false);
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (!world.isRemote) {
            TofuNetwork.Instance.unload(uuid, false);
        }
    }

    public boolean isRedstonePowered() {
        return this.world.isBlockPowered(this.pos);
    }
}