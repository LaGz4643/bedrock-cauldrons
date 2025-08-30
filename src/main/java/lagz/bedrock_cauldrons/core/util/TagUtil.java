package lagz.bedrock_cauldrons.core.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class TagUtil {
    public static boolean tagsContainEqualIntTag(@Nullable CompoundTag tag1, @Nullable CompoundTag tag2, String key) {
        return tagsContainEqualTag(tag1, tag2, key, Tag.TAG_ANY_NUMERIC, CompoundTag::getInt, integer -> false);
    }
    
    public static boolean tagsContainEqualListTag(@Nullable CompoundTag tag1, @Nullable CompoundTag tag2, String key, byte listType) {
        return tagsContainEqualTag(tag1, tag2, key, Tag.TAG_LIST, ((tag, k) -> tag.getList(k, listType)), ListTag::isEmpty);
    }
    
    public static <T> boolean tagsContainEqualTag(@Nullable CompoundTag tag1, @Nullable CompoundTag tag2, String key, byte type, BiFunction<CompoundTag, String, T> getValue, Predicate<T> isEmpty) {
        boolean tag1Null = tag1 == null;
        boolean tag2Null = tag2 == null;
        
        if (tag1Null ^ tag2Null) { // One tag is null
            CompoundTag nonNullTag = tag2Null ? tag1 : tag2; // Get non-null tag
            // Tags' values are equal if non-null tag does not contain value, or it's value is empty
            return !nonNullTag.contains(key, type) || isEmpty.test(getValue.apply(nonNullTag, key));
        } else if (tag1Null) { // Both tags are null
            return true; // Tags are equal
        }
        
        // Both tags are non-null
        
        boolean tag1Contains = tag1.contains(key, type);
        boolean tag2Contains = tag2.contains(key, type);
        
        if (tag1Contains && tag2Contains) { // Both contain tag
            return getValue.apply(tag1, key).equals(getValue.apply(tag2, key));
        } else if (!tag1Contains && !tag2Contains) { // Neither contain tag
            return true; // Tags are equal
        } else { // One contains tag
            CompoundTag containingTag = tag1Contains ? tag1 : tag2; // Get tag that contains
            // Tags' values are equal if containing tag's value is empty
            return isEmpty.test(getValue.apply(containingTag, key));
        }
    }
}
