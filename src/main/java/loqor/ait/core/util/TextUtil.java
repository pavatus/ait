package loqor.ait.core.util;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

import loqor.ait.tardis.Tardis;

public class TextUtil {

    public static Text forTardis(Tardis tardis) {
        String id = tardis.getUuid().toString();

        return Texts.bracketed(Text.literal(id)).styled(style -> style.withColor(Formatting.GREEN)
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, tardis.getUuid().toString()))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("message.ait.click_to_copy"))));
    }
}
