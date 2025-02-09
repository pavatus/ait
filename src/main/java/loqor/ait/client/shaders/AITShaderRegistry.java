package loqor.ait.client.shaders;

import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;

import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;

public class AITShaderRegistry {
    public static ShaderProgram BLIT_SCREEN_NO_BLEND;

    static {
        CoreShaderRegistrationCallback.EVENT.register(d -> {
            d.register(new Identifier(AITMod.MOD_ID, "blit_screen_noblend"),
                    VertexFormats.POSITION_TEXTURE_COLOR, shaderProgram -> {
                BLIT_SCREEN_NO_BLEND = shaderProgram;
            });
        });

        CoreShaderRegistrationCallback.EVENT.register(d -> {
            d.register(new Identifier(AITMod.MOD_ID, "rendertype_blur"),
                    VertexFormats.POSITION_TEXTURE_COLOR, shaderProgram -> {
                        BLIT_SCREEN_NO_BLEND = shaderProgram;
                    });
        });
    }
}
