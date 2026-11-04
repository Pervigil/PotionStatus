package dev.pervigil.potionstatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

public final class PotionStatusClient implements ClientModInitializer {
    private static final int PANEL_BACKGROUND = 0xB810141C;
    private static final int PANEL_BORDER = 0xFF75C9D8;
    private static final int TITLE_COLOR = 0xFFE8F5F7;
    private static final int NAME_COLOR = 0xFFF0F0F0;
    private static final int TIME_COLOR = 0xFFB8C5CC;
    private static final int ROW_HEIGHT = 12;
    private static final int PADDING = 7;

    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                ResourceLocation.fromNamespaceAndPath("potionstatus", "effects"),
                PotionStatusClient::render
        );
    }

    private static void render(GuiGraphics graphics, DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();

        if (client.player == null || client.options.hideGui) {
            return;
        }

        List<MobEffectInstance> effects = new ArrayList<>(client.player.getActiveEffects());
        if (effects.isEmpty()) {
            return;
        }

        effects.sort(Comparator.comparing(
                effect -> effect.getEffect().value().getDisplayName().getString(),
                String.CASE_INSENSITIVE_ORDER
        ));

        List<String> names = new ArrayList<>(effects.size());
        List<String> durations = new ArrayList<>(effects.size());
        int contentWidth = client.font.width("ACTIVE EFFECTS");

        for (MobEffectInstance effect : effects) {
            String name = effect.getEffect().value().getDisplayName().getString();
            if (effect.getAmplifier() > 0) {
                name += " " + romanLevel(effect.getAmplifier() + 1);
            }

            String duration = formatDuration(effect.getDuration());
            names.add(name);
            durations.add(duration);
            contentWidth = Math.max(contentWidth,
                    client.font.width(name) + 12 + client.font.width(duration));
        }

        int x = 8;
        int y = 8;
        int width = contentWidth + PADDING * 2;
        int height = PADDING * 2 + 10 + 4 + effects.size() * ROW_HEIGHT;

        graphics.fill(x, y, x + width, y + height, PANEL_BACKGROUND);
        graphics.fill(x, y, x + 2, y + height, PANEL_BORDER);

        graphics.drawString(client.font, "ACTIVE EFFECTS",
                x + PADDING, y + PADDING, TITLE_COLOR, false);

        int rowY = y + PADDING + 14;
        for (int i = 0; i < effects.size(); i++) {
            graphics.fill(x + PADDING, rowY + 2, x + PADDING + 3, rowY + 5,
                    effects.get(i).getEffect().value().isBeneficial()
                            ? 0xFF78D6A3 : 0xFFE6A06F);

            graphics.drawString(client.font, names.get(i),
                    x + PADDING + 7, rowY, NAME_COLOR, false);

            int durationWidth = client.font.width(durations.get(i));
            graphics.drawString(client.font, durations.get(i),
                    x + width - PADDING - durationWidth, rowY, TIME_COLOR, false);

            rowY += ROW_HEIGHT;
        }
    }

    private static String formatDuration(int ticks) {
        if (ticks < 0) {
            return "—";
        }

        int totalSeconds = ticks / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private static String romanLevel(int level) {
        return switch (level) {
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> Integer.toString(level);
        };
    }
}
