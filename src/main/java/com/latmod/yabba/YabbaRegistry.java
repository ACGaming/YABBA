package com.latmod.yabba;

import com.feed_the_beast.ftbl.lib.IconSet;
import com.latmod.yabba.api.IBarrelModel;
import com.latmod.yabba.api.IBarrelSkin;
import com.latmod.yabba.api.ITier;
import com.latmod.yabba.api.IYabbaRegistry;
import com.latmod.yabba.api.events.YabbaRegistryEvent;
import com.latmod.yabba.models.ModelBarrel;
import com.latmod.yabba.util.BarrelSkin;
import com.latmod.yabba.util.Tier;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 18.12.2016.
 */
public enum YabbaRegistry implements IYabbaRegistry
{
    INSTANCE;

    private static final Map<String, IBarrelSkin> SKINS = new HashMap<>();
    private static final Map<IBlockState, IBarrelSkin> SKINS_STATE_MAP = new HashMap<>();
    private static final Map<String, ITier> TIERS = new HashMap<>();
    private static final Map<String, IBarrelModel> MODELS = new HashMap<>();
    public static final List<IBarrelModel> ALL_MODELS = new ArrayList<>();
    public static final List<IBarrelSkin> ALL_SKINS = new ArrayList<>();

    public static final IBarrelSkin DEFAULT_SKIN = INSTANCE.addSkin(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK), "all=blocks/planks_oak");

    public void sendEvent()
    {
        addTier(Tier.WOOD);
        addModel(ModelBarrel.INSTANCE);

        MinecraftForge.EVENT_BUS.post(new YabbaRegistryEvent(this));
        ALL_MODELS.addAll(MODELS.values());
        ALL_SKINS.addAll(SKINS.values());

        Collections.sort(ALL_MODELS);
        Collections.sort(ALL_SKINS);

        Yabba.LOGGER.info("YABBA Models: " + ALL_MODELS.size());
        Yabba.LOGGER.info("YABBA Skins: " + ALL_SKINS.size());
        Yabba.LOGGER.info("YABBA Tiers: " + TIERS.size());
    }

    @Override
    public void addSkin(IBarrelSkin skin)
    {
        SKINS.put(skin.getName(), skin);
        SKINS_STATE_MAP.put(skin.getState(), skin);
    }

    @Override
    public IBarrelSkin addSkin(IBlockState parentState, String icons)
    {
        IBarrelSkin skin = new BarrelSkin(parentState, new IconSet(icons));
        addSkin(skin);
        return skin;
    }

    @Override
    public void addTier(ITier tier)
    {
        TIERS.put(tier.getName(), tier);
    }

    @Override
    public void addModel(IBarrelModel model)
    {
        MODELS.put(model.getName(), model);
    }

    public IBarrelSkin getSkin(String id)
    {
        IBarrelSkin skin = SKINS.get(id);
        return skin == null ? DEFAULT_SKIN : skin;
    }

    public IBarrelSkin getSkin(IBlockState id)
    {
        IBarrelSkin skin = SKINS_STATE_MAP.get(id);
        return skin == null ? DEFAULT_SKIN : skin;
    }

    public ITier getTier(String id)
    {
        ITier tier = TIERS.get(id);
        return tier == null ? Tier.WOOD : tier;
    }

    public boolean hasSkin(String id)
    {
        return SKINS.containsKey(id);
    }

    public IBarrelModel getModel(String id)
    {
        IBarrelModel model = MODELS.get(id);
        return model == null ? ModelBarrel.INSTANCE : model;
    }
}