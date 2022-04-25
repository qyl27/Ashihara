package kogasastudio.ashihara.utils;

import kogasastudio.ashihara.item.IHasCustomModel;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static kogasastudio.ashihara.helper.RenderHelper.XTP;

public class ItemDisplayPos
{
    public final ItemStackHandler handler;
    public int slot;

    private float[] pos = new float[3];
    private int range = 16;
    private Direction facing = Direction.NORTH;

    public ItemDisplayPos(ItemStackHandler handlerIn, int slotIn)
    {
        this.handler = handlerIn;
        this.slot = slotIn;
    }

    public ItemStack getDisplayStack() {return this.handler.getStackInSlot(this.slot).copy();}

    @Nullable
    public IHasCustomModel getItemCustomModel()
    {
        return this.getDisplayStack().getItem() instanceof IHasCustomModel ? (IHasCustomModel) this.getDisplayStack().getItem() : null;
    }

    public float[] getTranslation() {return this.pos;}

    public Direction getFacing() {return this.facing;}

    public float[] getPos(BlockPos posIn)
    {
        float[] pos = new float[3];
        pos[0] = posIn.getX() + this.pos[0];
        pos[1] = posIn.getY() + this.pos[1];
        pos[2] = posIn.getZ() + this.pos[2];
        return pos;
    }

    public float getScale() {return XTP(this.range);}

    public CompoundNBT serializeNBT(CompoundNBT compound)
    {
        compound.putInt("slotID", this.slot);
        compound.putInt("range", this.range);
        compound.putString("facing", this.facing.getString());
        compound.putFloat("x", this.pos[0]);
        compound.putFloat("y", this.pos[1]);
        compound.putFloat("z", this.pos[2]);

        return compound;
    }

    public void deserializeNBT(CompoundNBT compound)
    {
        this.slot = compound.getInt("slotID");
        this.range = compound.getInt("range");
        this.facing = Direction.valueOf(compound.getString("facing"));
        this.pos[0] = compound.getFloat("x");
        this.pos[1] = compound.getFloat("y");
        this.pos[2] = compound.getFloat("z");
    }
}