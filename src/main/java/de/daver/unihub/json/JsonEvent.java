package de.daver.unihub.json;

public interface JsonEvent {

    public String toJsonString();

    default String actionToString(Action action, String value){
        return "\"action\":\"" + action.toJsonString() + "\",\"value\":\"" + value + "\"}";
    }

}
