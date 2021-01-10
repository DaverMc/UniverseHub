package de.daver.unihub.json;

public class ClickEvent implements JsonEvent {

    private String json;

    public ClickEvent(Action action, String value){
        json = "\",\"clickEvent\":{";
        json = json + actionToString(action, value);
    }

    @Override
    public String toJsonString() {
        return json;
    }
}
