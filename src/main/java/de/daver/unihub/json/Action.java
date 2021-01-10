package de.daver.unihub.json;

public enum Action {

    CLICK_OPEN_URL("open_url"),
    CLICK_OPEN_FILE("open_file"),
    CLICK_RUN_COMMMAND("run_command"),
    CLICK_SUGGEST_COMMAND("suggest_command"),
    CLICK_CHANGE_PAGE("change_page"),
    CLICK_COPY_TO_CLIPBOARD("copy_to_clipboard"),

    HOVER_SHOW_TEXT("show_text"),
    HOVER_SHOW_ITEM("show_item"),
    HOVER_SHOW_ENTITY("show_entity");

    private String jsonText;

    Action(String jsonText){
        this.jsonText = jsonText;
    }

    public String toJsonString(){
        return jsonText;
    }

}
