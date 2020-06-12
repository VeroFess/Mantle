package slimeknights.mantle.client.screen.book.element;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;
import slimeknights.mantle.client.book.data.SectionData;

import java.util.ArrayList;
import java.util.List;

public class SelectionElement extends SizedBookElement {

  public static final int IMG_SIZE = 32;

  public static final int WIDTH = 42;
  public static final int HEIGHT = 42;

  private SectionData section;
  private ImageElement iconRenderer;

  private final int iconX;
  private final int iconY;

  public SelectionElement(int x, int y, SectionData section) {
    super(x, y, WIDTH, HEIGHT);

    this.section = section;

    this.iconX = this.x + WIDTH / 2 - IMG_SIZE / 2;
    this.iconY = this.y + HEIGHT / 2 - IMG_SIZE / 2;
    this.iconRenderer = new ImageElement(this.iconX, this.iconY, IMG_SIZE, IMG_SIZE, section.icon);
  }

  @Override
  public void draw(int mouseX, int mouseY, float partialTicks, FontRenderer fontRenderer) {
    boolean unlocked = this.section.isUnlocked(this.parent.advancementCache);
    boolean hover = this.isHovered(mouseX, mouseY);

    if (hover) {
      fill(this.iconX, this.iconY, this.iconX + IMG_SIZE, this.iconY + IMG_SIZE, this.parent.book.appearance.hoverColor);
    }
    if (unlocked) {
      RenderSystem.color4f(1F, 1F, 1F, hover ? 1F : 0.5F);
    }
    else {
      float r = ((this.parent.book.appearance.lockedSectionColor >> 16) & 0xff) / 255.F;
      float g = ((this.parent.book.appearance.lockedSectionColor >> 8) & 0xff) / 255.F;
      float b = (this.parent.book.appearance.lockedSectionColor & 0xff) / 255.F;
      RenderSystem.color4f(r, g, b, 0.75F);
    }

    this.iconRenderer.draw(mouseX, mouseY, partialTicks, fontRenderer);

    if (this.section.parent.appearance.drawSectionListText) {
      int textW = fontRenderer.getStringWidth(this.section.getTitle());
      int textX = this.x + WIDTH / 2 - textW / 2;
      int textY = this.y + HEIGHT - fontRenderer.FONT_HEIGHT / 2;
      fontRenderer.drawString(this.section.getTitle(),
              textX,
              textY,
              hover ? 0xFF000000 : 0x7F000000);
    }
  }

  @Override
  public void drawOverlay(int mouseX, int mouseY, float partialTicks, FontRenderer fontRenderer) {
    if (this.section != null && this.isHovered(mouseX, mouseY)) {
      List<String> text = new ArrayList<>();
      text.add(this.section.getTitle());
      if (!this.section.isUnlocked(this.parent.advancementCache)) {
        text.add(TextFormatting.RED + "Locked");
        text.add("Requirements:");

        text.addAll(this.section.requirements);
      }
      this.drawHoveringText(text, mouseX, mouseY, fontRenderer);
    }
  }

  @Override
  public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
    if (mouseButton == 0 && this.section != null && this.section.isUnlocked(this.parent.advancementCache) && this.isHovered(mouseX, mouseY)) {
      this.parent.openPage(this.parent.book.getFirstPageNumber(this.section, this.parent.advancementCache));
    }
  }
}