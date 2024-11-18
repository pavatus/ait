package loqor.ait.core.engine;

import net.minecraft.item.ItemStack;

import loqor.ait.core.AITTags;
import loqor.ait.core.util.ServerLifecycleHooks;

public abstract class DurableSubSystem extends SubSystem {
    private float durability = 0;

    protected DurableSubSystem(IdLike id) {
        super(id);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && this.isBroken()) return;

        super.setEnabled(enabled);
    }

    public float durability() {
        return durability;
    }

    private void setDurability(float durability) {
        float before = this.durability;

        this.durability = Math.max(0, Math.min(durability, 100));

        this.onDurabilityChange(before, this.durability);
    }
    public void addDurability(float durability) {
        this.setDurability(this.durability() + durability);
    }
    public void removeDurability(float durability) {
        this.addDurability(-durability);
    }

    public boolean isBroken() {
        return this.durability() <= 0;
    }

    protected void onDurabilityChange(float before, float after) {
        if (before == 0 && after > 0) {
            this.onRepair();
        } else if (before > 0 && after == 0) {
            this.onBreak();
        }

        this.sync();
    }
    protected void onBreak() {
        this.setEnabled(false);
    }

    protected void onRepair() {
        this.setEnabled(true);
    }
    protected int changeFrequency() {
        return 20;
    }
    protected abstract float cost();
    protected abstract boolean shouldDurabilityChange();

    public boolean isRepairItem(ItemStack stack) {
        return stack.isIn(AITTags.Items.REPAIRS_SUBSYSTEM);
    }

    @Override
    public void tick() {
        super.tick();

        if (!(this.isEnabled())) return;
        if (this.isBroken()) return;
        if (!ServerLifecycleHooks.isServer()) return;
        if (!this.shouldDurabilityChange()) return;
        if (ServerLifecycleHooks.get().getTicks() % this.changeFrequency() != 0) return;

        this.removeDurability(this.cost());
    }
}
