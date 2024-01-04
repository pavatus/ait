package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.managers.DeltaTimeManager;
import mdteam.ait.tardis.control.impl.RandomiserControl;
import mdteam.ait.tardis.control.impl.pos.PosManager;
import mdteam.ait.tardis.control.impl.pos.PosType;
import mdteam.ait.tardis.handler.TardisLink;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static mdteam.ait.AITMod.AIT_CONFIG;

// todo this class is like a monopoly, im gonna slash it into little corporate pieces
public class TardisTravel extends TardisLink {

    private State state = State.LANDED;
    private AbsoluteBlockPos.Directed position;
    private AbsoluteBlockPos.Directed destination;
    private PosManager posManager; // kinda useless everything in posmanager could just be done here but this class is getting bloated
    private AbsoluteBlockPos.Directed lastPosition;
    private boolean crashing = false;
    private static final int CHECK_LIMIT = AIT_CONFIG.SEARCH_HEIGHT(); // todo move into a config
    public static final int MAX_SPEED = 3;

    public TardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos) {
        super(tardis.getUuid());
        this.position = pos;
        if(this.lastPosition == null) this.lastPosition = pos;
    }

    public TardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos, AbsoluteBlockPos.Directed dest, State state) {
        super(tardis.getUuid());
        this.position = pos;
        if(this.lastPosition == null) this.lastPosition = pos;
        this.destination = dest;
        this.state = state;
    }

    public boolean isCrashing() {
        return this.crashing;
    }

    public void setPosition(AbsoluteBlockPos.Directed pos) {
        this.position = pos;
    }

    public void setLastPosition(AbsoluteBlockPos.Directed position) {
        this.lastPosition = position;
    }

    public AbsoluteBlockPos.Directed getLastPosition() {
        return lastPosition;
    }

    public AbsoluteBlockPos.Directed getPosition() {
        return position;
    }

    public AbsoluteBlockPos.Client getClientPosition() {
        return new AbsoluteBlockPos.Client(position, position.getDirection(), position.getWorld());
    }

    public Tardis getTardis() {
        if (this.tardisId == null && this.getPosition() != null) {
            Tardis found = TardisUtil.findTardisByPosition(this.getPosition());
            if (found != null)
                this.tardisId = found.getUuid();
        }

        return this.tardis();
    }

    /*public boolean ifNotInFlightDemat(ServerPlayerEntity player) {
        if (this.getState() == TardisTravel.State.LANDED) {
            if (this.getTardis().getFuel() <= 0) {
                player.sendMessage(Text.literal("The TARDIS is out of fuel and cannot dematerialise"));
                return false;
            }
            if (this.getTardis().isRefueling()) {
                player.sendMessage(Text.literal("The TARDIS cannot dematerialise when refueling"));
                return false;
            }
            this.dematerialise(PropertiesHandler.willAutoPilot(this.getTardis().getHandlers().getProperties()));
        } else if (this.getState() == TardisTravel.State.FLIGHT) {
            this.materialise();
            return true;
        }
        return true;
    }*/

    public void tick(MinecraftServer server) {
        this.tickDemat();
        this.tickMat();

        if (this.getSpeed() > 0 && this.getState() == State.LANDED) {
            this.dematerialise(PropertiesHandler.willAutoPilot(this.tardis().getHandlers().getProperties()));
        }
        if (this.getSpeed() == 0 && this.getState() == State.FLIGHT) {
            this.materialise();
        }
    }

    public void increaseSpeed() {
        PropertiesHandler.set(this.tardis().getHandlers().getProperties(), PropertiesHandler.SPEED, MathHelper.clamp(this.getSpeed() + 1,0, MAX_SPEED));
        this.tardis().markDirty();
    }
    public void decreaseSpeed() {
        PropertiesHandler.set(this.tardis().getHandlers().getProperties(), PropertiesHandler.SPEED, MathHelper.clamp(this.getSpeed() - 1,0, MAX_SPEED));
        this.tardis().markDirty();
    }
    public int getSpeed() {
        return PropertiesHandler.getInt(this.tardis().getHandlers().getProperties(), PropertiesHandler.SPEED);
    }
    private void setSpeed(int speed) {
        PropertiesHandler.set(this.tardis().getHandlers().getProperties(), PropertiesHandler.SPEED, speed);
        this.tardis().markDirty();
    }

    /**
     * Gets the number of ticks that the Tardis has been materialising for
     * @return ticks
     */
    public int getMatTicks() {
        return PropertiesHandler.getInt(this.getTardis().getHandlers().getProperties(), PropertiesHandler.MAT_TICKS);
    }
    private void setMatTicks(int ticks) {
        PropertiesHandler.set(this.getTardis().getHandlers().getProperties(), PropertiesHandler.MAT_TICKS, ticks);
        this.getTardis().markDirty();
    }
    private void tickMat() {
        if (this.getState() != State.MAT) {
            if (getMatTicks() != 0) setMatTicks(0);
            return;
        }

        setMatTicks(getMatTicks() + 1);

        if (getMatTicks() > (FlightUtil.getSoundLength(getMatSoundForCurrentState()) * 40)) {
            this.forceLand();
            this.setMatTicks(0);
        }
    }
    /**
     * Gets the number of ticks that the Tardis has been dematerialising for
     * @return ticks
     */
    public int getDematTicks() {
        return PropertiesHandler.getInt(this.getTardis().getHandlers().getProperties(), PropertiesHandler.DEMAT_TICKS);
    }
    private void setDematTicks(int ticks) {
        PropertiesHandler.set(this.getTardis().getHandlers().getProperties(), PropertiesHandler.DEMAT_TICKS, ticks);
        this.getTardis().markDirty();
    }
    private void tickDemat() {
        if (this.getState() != State.DEMAT) {
            if (getDematTicks() != 0) setDematTicks(0);
            return;
        }

        setDematTicks(getDematTicks() + 1);

        if (getDematTicks() > (FlightUtil.getSoundLength(getMatSoundForCurrentState()) * 40)) {
            this.toFlight();
            this.setDematTicks(0);
        }
    }
    /**
     * Performs a crash for the Tardis.
     * If the Tardis is not in flight state, the crash will not be executed.
     */
    public void crash() {
        // Check if Tardis is in flight state
        if (this.getState() != TardisTravel.State.FLIGHT) {
            return;
        }

        // If already crashing, return
        if (this.crashing) return;

        // Increment the position manager by 1000
        this.getPosManager().increment = 1000;
        // Randomize the Tardis destination
        // RandomiserControl.randomiseDestination(this.getTardis(), 10);
        // Play explosion sound and create explosion at console position if available
        if (this.getTardis().getDesktop().getConsolePos() != null) {
            TardisUtil.getTardisDimension().playSound(
                    null,
                    this.getTardis().getDesktop().getConsolePos(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE,
                    SoundCategory.BLOCKS,
                    3f,
                    1f
            );
            TardisUtil.getTardisDimension().createExplosion(
                    null,
                    null,
                    null,
                    this.getTardis().getDesktop().getConsolePos().toCenterPos(),
                    3f,
                    false,
                    World.ExplosionSourceType.TNT
            );
            Random random = new Random();
            for (PlayerEntity player : TardisUtil.getPlayersInInterior(this.getTardis())) {
                int x_random = random.nextInt(1, 10);
                int y_random = random.nextInt(1, 10);
                int z_random = random.nextInt(1, 10);

                boolean is_x_negative = false;
                boolean is_z_negative = false;
                if (random.nextInt(1,3) == 1) {
                    is_x_negative = true;
                }
                if (random.nextInt(1,3) == 1) {
                    is_z_negative = true;
                }
                player.addVelocity(0.5f * x_random * (is_x_negative ? -1 : 1), 0.25f * y_random, 0.5f * z_random * (is_z_negative ? -1 : 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0 , false, false));
            }
        }
        // Load the chunk of the Tardis destination
        this.getDestination().getWorld().getChunk(this.getTardis().getTravel().getDestination());
        // Enable alarm and disable anti-gravity properties for Tardis
        PropertiesHandler.set(this.getTardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED, true);
        PropertiesHandler.set(this.getTardis().getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED, false);
        // Set the destination position at the topmost block of the world at the X and Z coordinates of the destination
        this.setDestination(
                new AbsoluteBlockPos.Directed(
                        this.getTardis().getTravel().getDestination().getX(),
                        this.getDestination().getWorld().getTopY() - 1,
                        this.getDestination().getZ(),
                        this.getDestination().getWorld(),
                        this.getDestination().getDirection()
                ),
                true
        );
        this.crashing = true;
        // Mark Tardis as dirty
        this.getTardis().markDirty();
        // Remove fuel from Tardis
        this.getTardis().removeFuel(80);
        // Materialize the Tardis
        this.materialise();
        // Invoke the crash event
        TardisEvents.CRASH.invoker().onCrash(this.getTardis());
    }

    public void materialise() {
        this.materialise(false);
    }

    /**
     * Materialises the Tardis, bringing it to the specified destination.
     * This method handles the logic of materialization, including sound effects, locking the Tardis, and setting the Tardis state.
     */
    public void materialise(boolean ignoreChecks) {
        // Check if running on the client side, and if so, return early
        if (this.getDestination().getWorld().isClient()) {
            return;
        }

        // Disable autopilot
        PropertiesHandler.setAutoPilot(this.getTardis().getHandlers().getProperties(), false);

        // Get the server world of the destination
        ServerWorld world = (ServerWorld) this.getDestination().getWorld();
        world.getChunk(this.getDestination());

        this.setDestination(FlightUtil.getPositionFromPercentage(this.tardis().position(), this.tardis().destination(), this.tardis().getHandlers().getFlight().getDurationAsPercentage()), true);

        // Check if materialization is on cooldown and return if it is
        if (!ignoreChecks && FlightUtil.isMaterialiseOnCooldown(getTardis())) {
            return;
        }

        // Check if the Tardis materialization is prevented by event listeners
        if (!ignoreChecks && TardisEvents.MAT.invoker().onMat(getTardis())) {
            failToMaterialise();
            return;
        }

        // Lock the Tardis doors
        DoorHandler.lockTardis(true, this.getTardis(), null, true);

        // Set the Tardis state to materialize
        this.setState(State.MAT);

        // Get the server world of the destination
        ServerWorld destWorld = (ServerWorld) this.getDestination().getWorld();
        destWorld.getChunk(this.getDestination());

        // Play materialize sound at the destination
        this.getDestination().getWorld().playSound(null, this.getDestination(), this.getSoundForCurrentState(), SoundCategory.BLOCKS, 1f, 1f);

        // Play materialize sound at the Tardis console position if it exists
        if (this.getTardis() != null && this.getTardis().getDesktop().getConsolePos() != null) {
            TardisUtil.getTardisDimension().playSound(null, this.getTardis().getDesktop().getConsolePos(), this.getSoundForCurrentState(), SoundCategory.BLOCKS, 1f, 1f);
        }

        // Set the destination block to the Tardis exterior block
        ExteriorBlock block = (ExteriorBlock) AITBlocks.EXTERIOR_BLOCK;
        BlockState state = block.getDefaultState().with(Properties.HORIZONTAL_FACING, this.getDestination().getDirection());
        destWorld.setBlockState(this.getDestination(), state);

        // Create and add the exterior block entity at the destination
        ExteriorBlockEntity blockEntity = new ExteriorBlockEntity(this.getDestination(), state);
        destWorld.addBlockEntity(blockEntity);

        // Set the position of the Tardis to the destination
        this.setPosition(this.getDestination());

        // Run animations on the block entity
        this.runAnimations(blockEntity);

        // Schedule a timer task to transition to flight state after the materialize sound finishes playing
        // Replaced by the tick method, if you believe this to be better msg me on discord and tell me why
//        Timer animTimer = new Timer();
//        TardisTravel travel = this;
//
//        animTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (travel.getState() != State.MAT) {
//                    return;
//                }
//
//                travel.forceLand(blockEntity);
//            }
//        }, (long) FlightUtil.getSoundLength(this.getMatSoundForCurrentState()) * 1000L);
    }

    public void dematerialise(boolean withRemat) {
        if (!getTardis().hasPower()) {
            return; // no flying for you if you have no powa :)
        }
        if (this.getPosition().getWorld().isClient())
            return;

        if (PropertiesHandler.willAutoPilot(tardis().getHandlers().getProperties())) {
            // fufill all the prerequisites
            DoorHandler.lockTardis(true, tardis(), null, false);
            PropertiesHandler.setBool(tardis().getHandlers().getProperties(), PropertiesHandler.HANDBRAKE, false);
            tardis().setRefueling(false);
            if (this.getSpeed() == 0) this.increaseSpeed();
        }

        PropertiesHandler.setAutoPilot(this.getTardis().getHandlers().getProperties(), withRemat);

        ServerWorld world = (ServerWorld) this.getPosition().getWorld();
        world.getChunk(this.getPosition());

        if (FlightUtil.isDematerialiseOnCooldown(getTardis()))
            return; // cancelled

        // fixme where does this go?
        if (TardisEvents.DEMAT.invoker().onDemat(getTardis())) {
            failToTakeoff();
            return;
        }

        DoorHandler.lockTardis(true, this.getTardis(), null, true);

        this.setState(State.DEMAT);

        world.playSound(null, this.getPosition(), this.getSoundForCurrentState(), SoundCategory.BLOCKS);
        //TardisUtil.getTardisDimension().playSound(null, getInteriorCentre(), AITSounds.DEMAT, SoundCategory.BLOCKS, 10f, 1f);
        if (this.getTardis() != null)
            if (this.getTardis().getDesktop().getConsolePos() != null) {
                //System.out.println("FYI this should be working :p");
                TardisUtil.getTardisDimension().playSound(null, this.getTardis().getDesktop().getConsolePos(), this.getSoundForCurrentState(), SoundCategory.BLOCKS, 10f, 1f);
            }

        this.runAnimations();

        // A definite thing just in case the animation isnt run

        // Replaced by the tick method, if you believe this to be better msg me on discord and tell me why

//        Timer animTimer = new Timer();
//        TardisTravel travel = this;
//
//        animTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (travel.getState() != State.DEMAT)
//                    return;
//
//                travel.toFlight();
//            }
//        }, (long) FlightUtil.getSoundLength(this.getMatSoundForCurrentState()) * 1000L);
    }

    private void failToMaterialise() {
        // Play failure sound at the current position
        this.getPosition().getWorld().playSound(null, this.getPosition(), AITSounds.FAIL_MAT, SoundCategory.BLOCKS, 1f, 1f);

        // Play failure sound at the Tardis console position if the interior is not empty
        if (TardisUtil.isInteriorNotEmpty(tardis())) {
            TardisUtil.getTardisDimension().playSound(null, this.getTardis().getDesktop().getConsolePos(), AITSounds.FAIL_MAT, SoundCategory.BLOCKS, 1f, 1f);
        }

        // Send error message to the pilot
        TardisUtil.sendMessageToPilot(this.getTardis(), Text.literal("Unable to land!"));

        // Create materialization delay and return
        FlightUtil.createMaterialiseDelay(this.getTardis());
        return;
    }

    private void failToTakeoff() {
        // dont do anything if out of fuel, make it sad :(
        if (!tardis().hasPower()) return;

        // demat will be cancelled
        this.getPosition().getWorld().playSound(null, this.getPosition(), AITSounds.FAIL_DEMAT, SoundCategory.BLOCKS, 1f, 1f); // fixme can be spammed

        if (TardisUtil.isInteriorNotEmpty(tardis()))
            TardisUtil.getTardisDimension().playSound(null, this.getTardis().getDesktop().getConsolePos(), AITSounds.FAIL_DEMAT, SoundCategory.BLOCKS, 1f, 1f);

        TardisUtil.sendMessageToPilot(this.getTardis(), Text.literal("Unable to takeoff!")); // fixme translatable
        FlightUtil.createDematerialiseDelay(this.getTardis());
        return;
    }

    @NotNull
    private BlockPos getInteriorCentre() {
        BlockPos firstCorner = this.getTardis().getDesktop().getCorners().getFirst();
        BlockPos secondCorner = this.getTardis().getDesktop().getCorners().getSecond();
        // x^1 - x^2   z^1 - z^2
        // --------- , ---------
        //     2           2
        // firstCorner.getX() + secondCorner.getX()   firstCorner.getZ() + secondCorner.getZ()
        // ---------------------------------------- , ----------------------------------------
        //                    2                                          2
        return new BlockPos((firstCorner.getX() + secondCorner.getX()) / 2, firstCorner.getY(), (firstCorner.getZ() + secondCorner.getZ()) / 2);
    }

    /**
     * Checks whether the destination is valid otherwise searches for a new one
     *
     * @param limit     how many times the search can happen (should stop hanging)
     * @param fullCheck whether to search downwards or upwards
     * @return whether its safe to land
     */
    private boolean checkDestination(int limit, boolean fullCheck) {
        if (TardisUtil.isClient()) return true;

        ServerWorld world = (ServerWorld) this.getDestination().getWorld(); // this cast is fine, we know its server

        if (isDestinationTardisExterior()) { // fixme this portion of the code just deletes the other tardis' exterior!
            ExteriorBlockEntity target = (ExteriorBlockEntity) world.getBlockEntity(this.getDestination()); // safe

            if (target.getTardis() == null) return false;

            setDestinationToTardisInterior(target.getTardis(), true, 256); // how many times should this be

            return this.checkDestination(CHECK_LIMIT, PropertiesHandler.getBool(tardis().getHandlers().getProperties(), PropertiesHandler.FIND_GROUND)); // limit at a small number cus it might get too laggy
        }

        // is long line
        setDestination(new AbsoluteBlockPos.Directed(
                getDestination().getX(),
                MathHelper.clamp(getDestination().getY(), world.getBottomY(), world.getTopY() - 1),
                getDestination().getZ(),
                getDestination().getWorld(),
                getDestination().getDirection()),
                false
        );

        BlockPos.Mutable temp = this.getDestination().mutableCopy(); // loqor told me mutables were better, is this true? fixme if not

        if (fullCheck) {
            for (int i = 0; i < limit; i++) {
                if (world.getBlockState(temp).isReplaceable() && world.getBlockState(temp.up()).isReplaceable() && !world.getBlockState(temp.down()).isReplaceable()) { // check two blocks cus tardis is two blocks tall yk and check for groud
                    this.setDestination(new AbsoluteBlockPos.Directed(temp, world, this.getDestination().getDirection()), false);
                    return true;
                }

                temp = temp.down().mutableCopy();
            }

            temp = this.getDestination().mutableCopy();

            for (int i = 0; i < limit; i++) {
                if (world.getBlockState(temp).isReplaceable() && world.getBlockState(temp.up()).isReplaceable() && !world.getBlockState(temp.down()).isReplaceable()) { // check two blocks cus tardis is two blocks tall yk and check for groud
                    this.setDestination(new AbsoluteBlockPos.Directed(temp, world, this.getDestination().getDirection()), false);
                    return true;
                }

                temp = temp.up().mutableCopy();
            }
        }

        temp = this.getDestination().mutableCopy();

        return (world.getBlockState(temp).isReplaceable()) && (world.getBlockState(temp.up()).isReplaceable());
    }

    public boolean checkDestination() {
        return this.checkDestination(CHECK_LIMIT, PropertiesHandler.getBool(this.getTardis().getHandlers().getProperties(), PropertiesHandler.FIND_GROUND));
    }

    private boolean isDestinationTardisExterior() {
        ServerWorld world = (ServerWorld) this.getDestination().getWorld();

        // bad code but its 4am i cba anymore
        if (world.getBlockEntity(this.getDestination()) instanceof ExteriorBlockEntity) {
            return true;
        }

        if (world.getBlockEntity(this.getDestination().down()) instanceof ExteriorBlockEntity) {
            this.setDestination(new AbsoluteBlockPos.Directed(this.getDestination().down(), world, this.getDestination().getDirection()), false);
            return true;
        }

        return false;
    }

    /**
     * Picks a random pos within the placed tardis interior and sets the destination
     *
     * @param target tardis to land in
     * @param checks whether to run usual landing checks
     * @param limit  how many times to check / rerun this
     */
    private void setDestinationToTardisInterior(Tardis target, boolean checks, int limit) { // fixme as this causes problems sometimes
        if (target == null) return; // i hate null shit

        Random random = new Random();
        BlockPos h = TardisUtil.getPlacedInteriorCentre(target); // bad variable name
        h = PosType.X.add(h, random.nextBoolean() ? -random.nextInt(8) : random.nextInt(8));
        h = PosType.Z.add(h, random.nextBoolean() ? -random.nextInt(8) : random.nextInt(8));

        this.setDestination(new AbsoluteBlockPos.Directed(
                        h,
                        TardisUtil.getTardisDimension(),
                        this.getDestination().getDirection()),
                checks
        );
    }

    public void toFlight() {
        this.setLastPosition(this.getPosition());
        this.setState(TardisTravel.State.FLIGHT);
        this.deleteExterior();
        // this.checkShouldRemat();
    }

    public void forceLand(ExteriorBlockEntity blockEntity) {
        if (this.getState() != State.MAT)
            return;

        this.crashing = false;
        if (this.getSpeed() > 0) {
            this.setSpeed(0);
        }

        this.setState(TardisTravel.State.LANDED);
        if (blockEntity != null)
            this.runAnimations(blockEntity);
        if (DoorHandler.isClient()) return;
        DoorHandler.lockTardis(PropertiesHandler.getBool(this.getTardis().getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED), this.getTardis(), null, false);

        // fixme where does this go?
        TardisEvents.LANDED.invoker().onLanded(getTardis());

        // getPosition().getWorld().getChunkManager().getWorldChunk(getPosition().getX(), getPosition().getZ(), false);
    }

    public void forceLand() {
        this.forceLand(null);
    }

    public void runAnimations() {
        ServerWorld level = (ServerWorld) this.getPosition().getWorld();
        level.getChunk(this.getPosition());
        BlockEntity entity = level.getBlockEntity(this.getPosition());
        if (entity instanceof ExteriorBlockEntity exterior) {
            if (exterior.getAnimation() == null) return;

            exterior.getAnimation().setupAnimation(this.state);
            exterior.getAnimation().tellClientsToSetup(this.state);
        }
    }

    public void runAnimations(ExteriorBlockEntity exterior) {
        if (exterior.getAnimation() == null) return;

        exterior.getAnimation().setupAnimation(this.state);
        exterior.getAnimation().tellClientsToSetup(this.state);
    }

    public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
        this.destination = pos;

        // todo this sometimes resets it back to 0
        this.tardis().getHandlers().getFlight().recalculate();

        if (withChecks)
            this.checkDestination(CHECK_LIMIT, PropertiesHandler.getBool(this.getTardis().getHandlers().getProperties(), PropertiesHandler.FIND_GROUND));
    }

    public AbsoluteBlockPos.Directed getDestination() {
        if (this.destination == null) {
            if (this.getPosition() != null)
                this.destination = this.getPosition();
            else {
                // PANIC!!
                AITMod.LOGGER.error("Destination error! resetting to 0 0 0 in overworld");
                this.destination = new AbsoluteBlockPos.Directed(0, 0, 0, TardisUtil.findWorld(World.OVERWORLD), Direction.NORTH);
            }
        }

        return destination;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void toggleHandbrake() {
        this.state.next(new TravelContext(this, this.position, this.destination));
    }

    public void placeExterior() {
        this.position.setBlockState(AITBlocks.EXTERIOR_BLOCK.getDefaultState());
        ExteriorBlockEntity exterior = new ExteriorBlockEntity(
                this.position, this.position.getBlockState()
        );
        exterior.setTardis(this.getTardis());
        this.position.addBlockEntity(exterior);

        // jeeeez
        // boolean increaseCostOfTravelFromDimensionalTravel = lastPosition.getWorld().equals(this.position.getWorld());
        // double distance = Math.sqrt(this.position.getSquaredDistance(lastPosition.toCenterPos()));
        // double fuel_cost = (distance / 100) * (increaseCostOfTravelFromDimensionalTravel ? 10 : 1);
        // this.getTardis().removeFuel(fuel_cost);
    }

    public void deleteExterior() {
        this.getPosition().getWorld().getChunk(this.getPosition());
        this.getPosition().getWorld().removeBlock(this.getPosition(), false);
    }

    public void checkShouldRemat() {
        if (!PropertiesHandler.willAutoPilot(this.getTardis().getHandlers().getProperties()))
            return;

        this.materialise();
    }

    @NotNull
    public SoundEvent getSoundForCurrentState() {
        if (this.getTardis() != null)
            return this.getTardis().getExterior().getType().getSound(this.getState()).sound();
        return SoundEvents.INTENTIONALLY_EMPTY;
    }

    public MatSound getMatSoundForCurrentState() {
        if (this.getTardis() != null)
            return this.getTardis().getExterior().getType().getSound(this.getState());
        return AITSounds.LANDED_ANIM; // COUUULD be LANDED_ANIM but null is beteter
    }

    public PosManager getPosManager() {
        if (this.posManager == null)
            this.posManager = new PosManager();

        return this.posManager;
    }

    public enum State {
        LANDED(true) {
            @Override
            public void onEnable(TravelContext context) {
                AITMod.LOGGER.info("ON: LANDED");
            }

            @Override
            public void onDisable(TravelContext context) {
                AITMod.LOGGER.info("OFF: LANDED");
            }

            @Override
            public void schedule(TravelContext context) {

            }

            @Override
            public State getNext() {
                return DEMAT;
            }
        },
        DEMAT {
            @Override
            public void onEnable(TravelContext context) {
                AITMod.LOGGER.info("ON: DEMAT");

                // context.travel().dematerialise(false);
            }

            @Override
            public void onDisable(TravelContext context) {
                AITMod.LOGGER.info("OFF: DEMAT");
            }

            @Override
            public State getNext() {
                return FLIGHT;
            }
        },
        FLIGHT(true) {
            @Override
            public void onEnable(TravelContext context) {
                AITMod.LOGGER.info("ON: FLIGHT");
            }

            @Override
            public void onDisable(TravelContext context) {
                AITMod.LOGGER.info("OFF: LANDED");
            }

            @Override
            public void schedule(TravelContext context) {

            }

            @Override
            public State getNext() {
                return MAT;
            }
        },
        MAT {
            @Override
            public void onEnable(TravelContext context) {
                AITMod.LOGGER.info("ON: MAT");

                // context.travel().materialise();
            }

            @Override
            public void onDisable(TravelContext context) {
                AITMod.LOGGER.info("OFF: LANDED");
            }

            @Override
            public State getNext() {
                return LANDED;
            }
        },
        CRASH(true) {
            @Override
            public void onEnable(TravelContext context) {
                AITMod.LOGGER.info("ON: CRASH");

                // context.travel().materialise();
            }

            @Override
            public void onDisable(TravelContext context) {
                AITMod.LOGGER.info("OFF: LANDED");
            }

            @Override
            public void schedule(TravelContext context) {

            }

            @Override
            public State getNext() {
                return LANDED;
            }
        };

        private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        private final boolean isStatic;

        State(boolean isStatic) {
            this.isStatic = isStatic;
        }

        State() {
            this(false);
        }

        public boolean isStatic() {
            return isStatic;
        }

        public ScheduledExecutorService getService() {
            return service;
        }

        public abstract void onEnable(TravelContext context);

        public abstract void onDisable(TravelContext context);

        public abstract State getNext();

        public void next(TravelContext context) {
            this.service.shutdown();
            this.onDisable(context);

            State next = this.getNext();
            next.schedule(context);

            next.onEnable(context);
            context.travel().setState(next);
        }

        public void schedule(TravelContext context) {
        }
    }
}
