package de.daver.unihub.json;

public class HoverEvent implements JsonEvent {

    private String json;

    public HoverEvent(Action action, String value){
        json = "\",\"hoverEvent\":{";
        json = json + actionToString(action, value);
    }

    @Override
    public String toJsonString() {
        return json;
    }
}
