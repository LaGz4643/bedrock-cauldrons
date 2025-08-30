package lagz.bedrock_cauldrons.core.util;

import net.minecraft.world.item.DyeItem;

public class ColorUtil {
    
    public static int getDyeColor(DyeItem dyeitem) {
        return floatColorToIntColor(dyeitem.getDyeColor().getTextureDiffuseColors());
    }
    
    public static float[] intColorToFloatColor(int color) {
        float[] floatColor = new float[3];
        floatColor[0] = ((color & 16711680) >> 16) / 255.0F;
        floatColor[1] = ((color & '\uff00') >> 8) / 255.0F;
        floatColor[2] = (color & 255) / 255.0F;
        return floatColor;
    }
    
    public static int floatColorToIntColor(float[] color) {
        int r = (int) (color[0] * 255.0F);
        int g = (int) (color[1] * 255.0F);
        int b = (int) (color[2] * 255.0F);
        int intColor = (r << 8) + g;
        return (intColor << 8) + b;
    }
    
    public static int averageIntColors(int... colors) {
        int[] addedColors = new int[3];
        int addedMaxColor = 0;
        int colorCount = colors.length;
        
        for (int color : colors) {
            if (color == -1) {
                return -1;
            }
            
            int r = color >> 16 & 255;
            int g = color >> 8 & 255;
            int b = color & 255;
            
            addedMaxColor += Math.max(r, Math.max(g, b));
            addedColors[0] += r;
            addedColors[1] += g;
            addedColors[2] += b;
        }
        
        
        int averageRed = addedColors[0] / colorCount;
        int averageGreen = addedColors[1] / colorCount;
        int averageBlue = addedColors[2] / colorCount;
        
        float averageMaxColor = addedMaxColor / (float) colorCount;
        float maxAveragedColor = Math.max(averageRed, Math.max(averageGreen, averageBlue));
        
        averageRed = (int) (averageRed * averageMaxColor / maxAveragedColor);
        averageGreen = (int) (averageGreen * averageMaxColor / maxAveragedColor);
        averageBlue = (int) (averageBlue * averageMaxColor / maxAveragedColor);
        
        int averageColor = (averageRed << 8) + averageGreen;
        averageColor = (averageColor << 8) + averageBlue;
        return averageColor;
    }
}
