package de.daver.unihub.json;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;

public class JsonMessages {

    private JsonMessages(){}

    public static IChatBaseComponent createText(String[] lines){
        StringBuilder line = new StringBuilder("{\"text\":\"");
        for(String string : lines) line.append(string).append("\n");
        line = new StringBuilder((line.toString().endsWith("\n")) ? line.substring(0, line.length() - 1) : line.toString());
        return IChatBaseComponent.ChatSerializer.a(line + "\"}");
    }
}
