package com.teyvats.outrealm;

import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;

public final class TeyvatAdvancementLayout {
    private static final String TEYVAT_DELIGHT = "teyvat_delight";
    private static final String ROOT = "root";
    private static final String HEARTBEAT_MEMORY = "heartbeat_memory";
    private static final List<Slot> MAIN_ADVANCEMENTS = List.of(new Slot("elemental_taste", 1.0f, -3.0f), new Slot("food_archon", 2.0f, -3.0f), new Slot("got_teyvat_plant", 1.0f, -1.0f), new Slot("teyvat_botanist", 2.0f, -1.0f), new Slot("got_teyvat_biological_material", 1.0f, 0.0f), new Slot("teyvat_biologist", 2.0f, 0.0f), new Slot("got_teyvat_mineral", 1.0f, 1.0f), new Slot("teyvat_mineralogist", 2.0f, 1.0f), new Slot("got_mora", 1.0f, 2.0f), new Slot("got_primogem", 2.0f, 2.0f), new Slot("primogem_knife", 3.0f, 2.0f), new Slot("intertwined_fate", 1.0f, 4.0f), new Slot("heartbeat_memory", 2.0f, 4.0f));
    private static final List<Slot> SPECIAL_DISH_GOALS = List.of(new Slot("watch_out_for_the_cold", 0.0f, -1.0f), new Slot("benny_adventure_team_set_out", 1.0f, -1.0f), new Slot("bunny_baron_go", 2.0f, -1.0f), new Slot("no_alcohol_allowed", 3.0f, -1.0f), new Slot("distant_horizon", 4.0f, -1.0f), new Slot("slay_haishan_too", 0.0f, 0.0f), new Slot("tianquan_craft", 1.0f, 0.0f), new Slot("time_for_tea", 2.0f, 0.0f), new Slot("where_is_the_offal", 3.0f, 0.0f), new Slot("one_grill_of_kazuha", 0.0f, 1.0f), new Slot("tengu_not_dog", 1.0f, 1.0f), new Slot("energy_replenished", 2.0f, 1.0f), new Slot("thomas_kindness", 3.0f, 1.0f), new Slot("can_this_really_be_eaten", 0.0f, 2.0f), new Slot("gentlemans_salute", 0.0f, 3.0f));

    private TeyvatAdvancementLayout() {
    }

    public static void arrange(ServerAdvancementManager manager) {
        Optional<DisplayInfo> tabRoot = TeyvatAdvancementLayout.display(manager, TEYVAT_DELIGHT);
        Optional<DisplayInfo> root = TeyvatAdvancementLayout.display(manager, ROOT);
        if (tabRoot.isEmpty() || root.isEmpty()) {
            return;
        }
        root.get().setLocation(tabRoot.get().getX() + 1.0f, tabRoot.get().getY());
        float rootX = root.get().getX();
        float rootY = root.get().getY();
        for (Slot slot : MAIN_ADVANCEMENTS) {
            TeyvatAdvancementLayout.place(manager, slot.id(), rootX + slot.x(), rootY + slot.y());
        }
        Optional<DisplayInfo> anchor = TeyvatAdvancementLayout.display(manager, HEARTBEAT_MEMORY);
        if (anchor.isEmpty()) {
            return;
        }
        float baseX = anchor.get().getX() + 1.0f;
        float baseY = anchor.get().getY();
        for (Slot slot : SPECIAL_DISH_GOALS) {
            TeyvatAdvancementLayout.place(manager, slot.id(), baseX + slot.x(), baseY + slot.y());
        }
    }

    private static void place(ServerAdvancementManager manager, String id, float x, float y) {
        TeyvatAdvancementLayout.display(manager, id).ifPresent(display -> display.setLocation(x, y));
    }

    private static Optional<DisplayInfo> display(ServerAdvancementManager manager, String id) {
        ResourceLocation key = new ResourceLocation("teyvats_delight_meeting_in_outrealm", "main/" + id);
        for (Advancement advancement : manager.getAllAdvancements()) {
            if (advancement.getId().equals(key)) {
                return Optional.ofNullable(advancement.getDisplay());
            }
        }
        return Optional.empty();
    }

    private record Slot(String id, float x, float y) {
    }
}

