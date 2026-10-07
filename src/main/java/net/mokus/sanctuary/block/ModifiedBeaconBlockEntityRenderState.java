package net.mokus.sanctuary.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.joml.Vector3f;

public class ModifiedBeaconBlockEntityRenderState extends BlockEntityRenderState {
    public final ItemStackRenderState swordRenderState = new ItemStackRenderState();
    public float plantYaw;
    public Vector3f plunge;
    public float cubeSize;
}
