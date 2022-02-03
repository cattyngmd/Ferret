package wtf.cattyn.ferret.api.feature.script.lua.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import wtf.cattyn.ferret.common.Globals;

public class LuaInteractions implements Globals {

    private static LuaInteractions instance;

    LuaInteractions() {
        super();
    }

    public static LuaInteractions getDefault() {
        if (instance == null) instance = new LuaInteractions();
        return instance;
    }

    public boolean canBreak(BlockPos blockPos, BlockState state) {
        if (!mc.player.isCreative() && state.getHardness(mc.world, blockPos) < 0) return false;
        return state.getOutlineShape(mc.world, blockPos) != VoxelShapes.empty();
    }

    public boolean isPlaceable(BlockPos pos, boolean entityCheck) {
        if (!mc.world.getBlockState(pos).getMaterial().isReplaceable()) return false;
        for (Entity e : mc.world.getEntitiesByClass(Entity.class, new Box(pos), e -> !(e instanceof ExperienceBottleEntity || e instanceof ItemEntity || e instanceof ExperienceOrbEntity))) {
            if (e instanceof PlayerEntity) return false;
            return !entityCheck;
        }
        return true;
    }

    public boolean breakBlock(BlockPos pos) {
        if (!canBreak(pos, mc.world.getBlockState(pos))) return false;
        BlockPos bp = pos instanceof BlockPos.Mutable ? new BlockPos(pos) : pos;

        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, bp, Direction.UP));
        mc.player.swingHand(Hand.MAIN_HAND);
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, bp, Direction.UP));

        mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

        return true;
    }

    public boolean useItem(BlockPos pos) {
        return useItem(pos, Hand.MAIN_HAND);
    }

    public boolean useItem(BlockPos pos, Hand hand) {
        if (mc.world == null || mc.player == null || mc.interactionManager == null) return false;
        Direction direction = mc.crosshairTarget != null ? (( BlockHitResult ) mc.crosshairTarget).getSide() : Direction.DOWN;
        ActionResult result = mc.interactionManager.interactBlock(mc.player, mc.world, hand, new BlockHitResult(
                Vec3d.ofCenter(pos),  direction, pos, false
        ));
        if (result.shouldSwingHand()) {
            mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
        }
        return true;
    }

    public boolean place(BlockPos pos, boolean airPlace) {
        return place(pos, airPlace, Hand.MAIN_HAND);
    }

    public boolean place(BlockPos pos, boolean airPlace, Hand hand) {
        if (mc.world == null || mc.player == null || mc.interactionManager == null) return false;
        if (!isPlaceable(pos, false)) return false;
        Direction direction = calcSide(pos);
        if (direction == null) {
            if (airPlace) direction = mc.crosshairTarget != null ? (( BlockHitResult ) mc.crosshairTarget).getSide() : Direction.DOWN;
            else return false;
        }
        BlockPos bp = airPlace ? pos : pos.offset(direction);
        ActionResult result = mc.interactionManager.interactBlock(mc.player, mc.world, hand, new BlockHitResult(
                Vec3d.ofCenter(pos),  airPlace ? direction : direction.getOpposite(), bp, false
        ));
        if (result.shouldSwingHand()) {
            mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
        }
        return true;
    }

    public Direction calcSide(BlockPos pos) {
        for (Direction d : Direction.values())
            if (!mc.world.getBlockState(pos.add(d.getVector())).getMaterial().isReplaceable()) return d;
        return null;
    }

    public double getBlockBreakingSpeed(int slot, BlockPos pos) {
        return getBlockBreakingSpeed(slot, mc.world.getBlockState(pos));
    }

    public double getBlockBreakingSpeed(int slot, BlockState block) {
        double speed = mc.player.getInventory().main.get(slot).getMiningSpeedMultiplier(block);

        if (speed > 1) {
            ItemStack tool = mc.player.getInventory().getStack(slot);

            int efficiency = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, tool);

            if (efficiency > 0 && !tool.isEmpty()) speed += efficiency * efficiency + 1;
        }

        if (StatusEffectUtil.hasHaste(mc.player)) {
            speed *= 1 + (StatusEffectUtil.getHasteAmplifier(mc.player) + 1) * 0.2F;
        }

        if (mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            float k = switch (mc.player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };

            speed *= k;
        }

        if (mc.player.isSubmergedIn(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(mc.player)) {
            speed /= 5.0F;
        }

        if (!mc.player.isOnGround()) {
            speed /= 5.0F;
        }

        float hardness = block.getHardness(null, null);
        if (hardness == -1) return 0;

        speed /= hardness / (!block.isToolRequired() || mc.player.getInventory().main.get(slot).isSuitableFor(block) ? 30 : 100);

        float ticks = ( float ) (Math.floor(1.0f / speed) + 1.0f);

        return ( long ) ((ticks / 20.0f) * 1000);
    }

    public Direction right(Direction direction) {
        return switch (direction) {
            case EAST  -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
            case NORTH -> Direction.EAST;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }

    public Direction left(Direction direction) {
        return switch (direction) {
            case EAST  -> Direction.NORTH;
            case NORTH -> Direction.WEST;
            case WEST -> Direction.SOUTH;
            case SOUTH -> Direction.EAST;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }

}
