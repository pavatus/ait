package loqor.ait.core.blockentities;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITItems;
import loqor.ait.core.blocks.WaypointBankBlock;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.Waypoint;
import loqor.ait.core.item.WaypointItem;
import loqor.ait.tardis.link.v2.block.InteriorLinkableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WaypointBankBlockEntity extends InteriorLinkableBlockEntity {

    private final WaypointData[] waypoints = new WaypointData[WaypointBankBlock.MAX_COUNT];
    private int selected = -1;

    public WaypointBankBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.WAYPOINT_BANK_BLOCK_ENTITY_TYPE, pos, state);
    }

    public ActionResult onUse(World world, BlockState state, PlayerEntity player, Hand hand, int slot) {
        ItemStack stack = player.getStackInHand(hand);

        if (world.isClient())
            return ActionResult.SUCCESS;

        if (stack.getItem() instanceof WaypointItem)
            return this.insert(state, stack, slot);

        if (player.isSneaking())
            return this.select(state, slot);

        return this.take(state, player, slot);
    }

    private ActionResult take(BlockState state, PlayerEntity player, int slot) {
        if (this.selected != slot)
            return ActionResult.FAIL;

        WaypointData waypoint = this.waypoints[slot];

        if (waypoint == null)
            return ActionResult.FAIL;

        this.waypoints[slot] = null;
        this.sync(state);

        player.dropItem(waypoint.toStack(), true);
        return ActionResult.SUCCESS;
    }

    private ActionResult insert(BlockState state, ItemStack stack, int slot) {
        this.waypoints[slot] = WaypointData.fromStack(stack);
        this.sync(state);

        return ActionResult.SUCCESS;
    }

    private ActionResult select(BlockState state, int slot) {
        this.selected = slot;
        this.sync(state);

        return ActionResult.SUCCESS;
    }

    protected void sync(BlockState state) {
        this.markDirty();
        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        NbtList waypoints = nbt.getList("waypoints", NbtElement.COMPOUND_TYPE);

        for (int i = 0; i < this.waypoints.length; i++) {
            this.waypoints[i] = WaypointData.fromNbt(waypoints.getCompound(i));
        }

        this.selected = nbt.getShort("selected");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        NbtList waypoints = new NbtList();

        for (int i = 0; i < this.waypoints.length; i++) {
            WaypointData data = this.waypoints[i];
            NbtCompound compound = new NbtCompound();

            if (data != null)
                data.toNbt(compound);

            waypoints.add(i, compound);
        }

        nbt.put("waypoints", waypoints);
        nbt.putShort("selected", (short) selected);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public DoubleBlockHalf getHalf() {
        return null;
    }

    public WaypointData[] getWaypoints() {
        return waypoints;
    }

    public int getSelected() {
        return selected;
    }

    public record WaypointData(int color, String name, DirectedGlobalPos pos) {

        private WaypointData(int color, Waypoint waypoint) {
            this(color, waypoint.name(), waypoint.getPos());
        }

        public static WaypointData fromStack(ItemStack stack) {
            int color = ((DyeableItem) AITItems.WAYPOINT_CARTRIDGE).getColor(stack);
            Waypoint waypoint = Waypoint.fromStack(stack);

            return new WaypointData(color, waypoint);
        }

        public ItemStack toStack() {
            ItemStack result = new ItemStack(AITItems.WAYPOINT_CARTRIDGE);

            WaypointItem.setPos(result, this.pos);
            result.setCustomName(Text.literal(this.name));

            ((DyeableItem) AITItems.WAYPOINT_CARTRIDGE).setColor(result, this.color);
            return result;
        }

        public void toNbt(NbtCompound nbt) {
            nbt.putInt("color", this.color);
            nbt.putString("name", this.name);
            if (this.pos != null)
                nbt.put("pos",this.pos.toNbt());
        }

        public static WaypointData fromNbt(NbtCompound nbt) {
            if (nbt.isEmpty())
                return null;

            int color = nbt.getInt("color");
            String name = nbt.getString("name");
            DirectedGlobalPos pos = DirectedGlobalPos.fromNbt(nbt.getCompound("pos"));

            return new WaypointData(color, name, pos);
        }
    }
}