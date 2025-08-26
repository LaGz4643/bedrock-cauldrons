package lagz.bedrock_cauldrons.core.mixin;

import lagz.bedrock_cauldrons.common.block.entity.PotionCauldronBlockEntity;
import lagz.bedrock_cauldrons.core.registry.BCBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.SwampHutPiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SwampHutPiece.class)
public abstract class SwampHutPieceMixin extends ScatteredFeaturePiece {
    @Unique
    private boolean bedrock_cauldrons$placedCauldron;
    
    protected SwampHutPieceMixin(StructurePieceType p_209920_, int p_209921_, int p_209922_, int p_209923_, int p_209924_, int p_209925_, int p_209926_, Direction p_209927_) {
        super(p_209920_, p_209921_, p_209922_, p_209923_, p_209924_, p_209925_, p_209926_, p_209927_);
    }
    
    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    private void onLoad(CompoundTag tag, CallbackInfo ci) {
        this.bedrock_cauldrons$placedCauldron = tag.getBoolean("bedrock_cauldrons:placed_cauldron");
    }
    
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void onSave(StructurePieceSerializationContext context, CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("bedrock_cauldrons:placed_cauldron", this.bedrock_cauldrons$placedCauldron);
    }
    
    @Redirect(method = "postProcess", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/SwampHutPiece;placeBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/block/state/BlockState;IIILnet/minecraft/world/level/levelgen/structure/BoundingBox;)V", ordinal = 7))
    private void redirectCauldronBlockState(SwampHutPiece instance, WorldGenLevel worldGenLevel, BlockState state, int x, int y, int z, BoundingBox boundingBox) {}
    
    @Inject(method = "postProcess", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/SwampHutPiece;placeBlock(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/block/state/BlockState;IIILnet/minecraft/world/level/levelgen/structure/BoundingBox;)V", ordinal = 7))
    private void tryPlaceCauldron(WorldGenLevel level, StructureManager p_229962_, ChunkGenerator p_229963_, RandomSource randomsource, BoundingBox boundingbox, ChunkPos p_229966_, BlockPos p_229967_, CallbackInfo ci) {
        if (!this.bedrock_cauldrons$placedCauldron) {
            BlockPos blockpos = this.getWorldPos(4, 2, 6);
            if (boundingbox.isInside(blockpos) && !level.getBlockState(blockpos).is(BCBlocks.POTION_CAULDRON.get())) {
                this.placeBlock(level, BCBlocks.POTION_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, randomsource.nextInt(1, 4)), 4, 2, 6, boundingbox);
                if (level.getBlockEntity(blockpos) instanceof PotionCauldronBlockEntity entity) {
                    entity.initRandomPotionStack(randomsource);
                }
                this.bedrock_cauldrons$placedCauldron = true;
            }
        }
    }
}
