package baguchan.mcmod.tofucraft.client.render.layer;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class GlowLayer<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    private final ResourceLocation TEXTURES;

    public GlowLayer(IEntityRenderer<T, M> p_i50921_1_, ResourceLocation resourceLocation) {
        super(p_i50921_1_);
        TEXTURES = resourceLocation;
    }

    public void render(T entityIn, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
        this.bindTexture(TEXTURES);
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        if (entityIn.isInvisible()) {
            GlStateManager.depthMask(false);
        } else {
            GlStateManager.depthMask(true);
        }

        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) j, (float) k);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
        gamerenderer.setupFogColor(true);
        this.getEntityModel().render(entityIn, p_212842_2_, p_212842_3_, p_212842_5_, p_212842_6_, p_212842_7_, p_212842_8_);
        gamerenderer.setupFogColor(false);
        i = entityIn.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) j, (float) k);
        this.func_215334_a(entityIn);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
