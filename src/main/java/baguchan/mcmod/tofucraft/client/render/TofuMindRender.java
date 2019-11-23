package baguchan.mcmod.tofucraft.client.render;

import baguchan.mcmod.tofucraft.TofuCraftCore;
import baguchan.mcmod.tofucraft.client.model.TofuMindModel;
import baguchan.mcmod.tofucraft.client.render.layer.GlowLayer;
import baguchan.mcmod.tofucraft.entity.TofuMindEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TofuMindRender extends MobRenderer<TofuMindEntity, TofuMindModel<TofuMindEntity>> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(TofuCraftCore.MODID, "textures/mob/tofumind/tofumind.png");

    public TofuMindRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new TofuMindModel<>(), 0.5F);
        this.addLayer(new GlowLayer<>(this, new ResourceLocation(TofuCraftCore.MODID, "textures/mob/tofumind/tofumind_eye.png")));
    }

    protected ResourceLocation getEntityTexture(TofuMindEntity entity) {
        return TEXTURES;
    }
}