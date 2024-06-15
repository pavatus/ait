package loqor.ait.core.data;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Unmodifiable;

/**
 * This class should be immutable. It contains the BlockPos and a Dimension of the block position.
 * @deprecated Use {@link net.minecraft.util.math.GlobalPos} instead.
 */
@Deprecated
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

	public void setBlockState(BlockState state, int flags) {
		this.getWorld().setBlockState(this, state, flags);
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

		private final int rotation;

		public Directed(int x, int y, int z, SerialDimension dimension, int rotation) {
			super(x, y, z, dimension);

			this.rotation = rotation;
		}

		public Directed(BlockPos pos, SerialDimension dimension, int rotation) {
			this(pos.getX(), pos.getY(), pos.getZ(), dimension, rotation);
		}

		public Directed(AbsoluteBlockPos pos, int rotation) {
			this(pos, pos.getDimension(), rotation);
		}

		public Directed(int x, int y, int z, World world, int rotation) {
			super(x, y, z, world);

			this.rotation = rotation;
		}

		public Directed(BlockPos pos, World world, int rotation) {
			this(pos.getX(), pos.getY(), pos.getZ(), world, rotation);
		}

		public int getRotation() {
			return rotation;
		}

		public Vec3i getVector(int rotation) {
			return switch (rotation) {
				default -> new Vec3i(0, 0, 0);
				case 0 -> Direction.NORTH.getVector();
				case 1, 2, 3 -> Direction.NORTH.getVector().add(Direction.EAST.getVector());
				case 4 -> Direction.EAST.getVector();
				case 5, 6, 7 -> Direction.EAST.getVector().add(Direction.SOUTH.getVector());
				case 8 -> Direction.SOUTH.getVector();
				case 9, 10, 11 -> Direction.SOUTH.getVector().add(Direction.WEST.getVector());
				case 12 -> Direction.WEST.getVector();
				case 13, 14, 15 -> Direction.NORTH.getVector().add(Direction.SOUTH.getVector());
			};
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof AbsoluteBlockPos.Directed other)
				return this.rotation == other.rotation && super.equals(other);

			return super.equals(o);
		}

		@Override
		public NbtCompound toNbt() {
			NbtCompound nbt = super.toNbt();

			nbt.putInt("rotation", this.rotation);

			return nbt;
		}

		public static Directed fromNbt(NbtCompound nbt) {
			AbsoluteBlockPos pos = AbsoluteBlockPos.fromNbt(nbt);

			int dir = nbt.getInt("rotation");

			return new Directed(pos, dir);
		}
	}
}

