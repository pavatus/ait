package loqor.ait.core.data;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Unmodifiable;

/**
 * This class should be immutable. It contains the BlockPos and a Dimension of the block position.
 */
@Unmodifiable
public class AbsoluteBlockPos extends BlockPos {

	private final SerialDimension dimension;

	public AbsoluteBlockPos(int x, int y, int z, SerialDimension dimension) {
		super(x, y, z);

		this.dimension = dimension;
	}

	public AbsoluteBlockPos(BlockPos pos, SerialDimension dimension) {
		this(pos.getX(), pos.getY(), pos.getZ(), dimension);
	}

	public AbsoluteBlockPos(int x, int y, int z, World world) {
		this(x, y, z, new SerialDimension(world));
	}

	public AbsoluteBlockPos(BlockPos pos, World world) {
		this(pos.getX(), pos.getY(), pos.getZ(), world);
	}

	public World getWorld() {
		return dimension.get();
	}

	public SerialDimension getDimension() {
		return dimension;
	}

	public BlockEntity getBlockEntity() {
		return this.getWorld().getBlockEntity(this);
	}

	public void addBlockEntity(BlockEntity blockEntity) {
		this.getWorld().addBlockEntity(blockEntity);
	}

	public BlockState getBlockState() {
		return this.getWorld().getBlockState(this);
	}

	public void setBlockState(BlockState state) {
		this.getWorld().setBlockState(this, state);
	}

	public Chunk getChunk() {
		return this.getWorld().getChunk(this);
	}

	public AbsoluteBlockPos above() {
		return new AbsoluteBlockPos(this.getX(), this.getY() + 1, this.getZ(), this.getWorld());
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();

		nbt.put("pos", NbtHelper.fromBlockPos(this));
		nbt.putString("dimension", this.getDimension().getValue());

		return nbt;
	}

	public static AbsoluteBlockPos fromNbt(NbtCompound nbt) {
		BlockPos pos = NbtHelper.toBlockPos(nbt.getCompound("pos"));
		SerialDimension dimension = new SerialDimension(nbt.getString("dimension"));
		return new AbsoluteBlockPos(pos, dimension);
	}

	@Override
	public String toString() {
		return "AbsoluteBlockPos[ " + getX() + " _ " + getY() + " _ " + getZ() + " ] | [ " + getWorld() + " ]";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof AbsoluteBlockPos absolute)
			return this.getDimension().equals(absolute.getDimension()) && super.equals(absolute);

		return super.equals(o);
	}

	public static class Directed extends AbsoluteBlockPos {

		private final Direction direction;

		public Directed(int x, int y, int z, SerialDimension dimension, Direction direction) {
			super(x, y, z, dimension);

			this.direction = direction;
		}

		public Directed(BlockPos pos, SerialDimension dimension, Direction direction) {
			this(pos.getX(), pos.getY(), pos.getZ(), dimension, direction);
		}

		public Directed(AbsoluteBlockPos pos, Direction direction) {
			this(pos, pos.getDimension(), direction);
		}

		public Directed(int x, int y, int z, World world, Direction direction) {
			super(x, y, z, world);

			this.direction = direction;
		}

		public Directed(BlockPos pos, World world, Direction direction) {
			this(pos.getX(), pos.getY(), pos.getZ(), world, direction);
		}

		public Direction getDirection() {
			return direction;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof AbsoluteBlockPos.Directed other)
				return this.direction == other.direction && super.equals(other);

			return super.equals(o);
		}

		@Override
		public NbtCompound toNbt() {
			NbtCompound nbt = super.toNbt();

			nbt.putInt("direction", this.direction.getId());

			return nbt;
		}

		public static Directed fromNbt(NbtCompound nbt) {
			AbsoluteBlockPos pos = AbsoluteBlockPos.fromNbt(nbt);

			Direction dir = Direction.byId(nbt.getInt("direction"));

			return new Directed(pos, dir);
		}
	}

	// i dont understand how to use this class, it needs documentation. I don't understand why the world variable is there if the client can't get worlds other than its own
	public static class Client extends AbsoluteBlockPos.Directed {
		private final World world;

		public Client(AbsoluteBlockPos pos, Direction direction, World world) {
			super(pos, direction);

			this.world = world;
		}

		public World getClientWorld() {
			return this.world;
		}

		public Client(int x, int y, int z, World world, Direction direction) {
			super(x, y, z, world, direction);

			this.world = world;
		}
	}
}

