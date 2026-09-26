package com.teyvats.outrealm;

import com.teyvats.outrealm.StarconchEntity;
import com.teyvats.outrealm.StarconchModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class StarconchRenderer
extends MobRenderer<StarconchEntity, StarconchModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"teyvats_delight_meeting_in_outrealm", (String)"textures/entity/starconch.png");

    public StarconchRenderer(EntityRendererProvider.Context context) {
        super(context, new StarconchModel(context.bakeLayer(StarconchModel.LAYER_LOCATION)), 0.3f);
    }

    public ResourceLocation getTextureLocation(StarconchEntity entity) {
        return TEXTURE;
    }
}

