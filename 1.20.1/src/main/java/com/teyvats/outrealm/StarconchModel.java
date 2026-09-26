package com.teyvats.outrealm;

import com.teyvats.outrealm.StarconchEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class StarconchModel
extends EntityModel<StarconchEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation((String)"teyvats_delight_meeting_in_outrealm", (String)"starconch"), "main");
    private final ModelPart bone;
    private final ModelPart star;

    public StarconchModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.star = this.bone.getChild("\u661f\u661f");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0f, -3.0f, -6.0f, 7.0f, 3.0f, 12.0f, new CubeDeformation(0.0f)).mirror(false).texOffs(-2, 22).mirror().addBox(-6.0f, -3.0f, -4.0f, 5.0f, 3.0f, 10.0f, new CubeDeformation(0.0f)).mirror(false).texOffs(26, 24).mirror().addBox(-4.0f, -5.0f, -1.0f, 5.0f, 1.0f, 5.0f, new CubeDeformation(0.0f)).mirror(false), PartPose.offsetAndRotation((float)0.0f, (float)25.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)-0.2182f));
        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 15).mirror().addBox(-4.0f, -1.0f, -8.0f, 8.0f, 1.0f, 8.0f, new CubeDeformation(0.0f)).mirror(false), PartPose.offsetAndRotation((float)-1.0f, (float)-3.0f, (float)-3.0f, (float)0.0f, (float)-3.1416f, (float)0.0f));
        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(26, 30).mirror().addBox(0.0f, -4.0f, -6.0f, 1.0f, 5.0f, 6.0f, new CubeDeformation(0.0f)).mirror(false), PartPose.offsetAndRotation((float)-1.0f, (float)0.0f, (float)-2.0f, (float)0.0f, (float)0.1745f, (float)0.0f));
        PartDefinition starPart = bone.addOrReplaceChild("\u661f\u661f", CubeListBuilder.create().texOffs(32, 15).mirror().addBox(3.0f, -1.0f, 1.0f, 1.0f, 1.0f, 3.0f, new CubeDeformation(0.0f)).mirror(false).texOffs(32, 22).mirror().addBox(2.0f, -1.0f, 2.0f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)).mirror(false).texOffs(0, 35).mirror().addBox(5.0f, -1.0f, 2.0f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)).mirror(false).texOffs(32, 19).mirror().addBox(4.0f, -1.0f, 1.0f, 1.0f, 1.0f, 2.0f, new CubeDeformation(0.0f)).mirror(false).texOffs(4, 35).mirror().addBox(5.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)).mirror(false).texOffs(8, 35).mirror().addBox(2.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, new CubeDeformation(0.0f)).mirror(false), PartPose.offsetAndRotation((float)-4.0f, (float)-5.0f, (float)6.0f, (float)0.0f, (float)1.5708f, (float)0.0f));
        return LayerDefinition.create((MeshDefinition)meshdefinition, (int)64, (int)64);
    }

    public void setupAnim(StarconchEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

