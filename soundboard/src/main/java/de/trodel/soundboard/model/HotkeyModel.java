package de.trodel.soundboard.model;

import org.json.JSONArray;

public class HotkeyModel {

    private final int[] hotkey;

    public HotkeyModel(int[] hotkey) {
        this.hotkey = hotkey;
    }

    public int[] getHotkey() {
        return hotkey;
    }

    public static JSONArray toJson(HotkeyModel model) {
        return new JSONArray(model.hotkey);
    }

    public static HotkeyModel fromJson(JSONArray arr) {
        int[] result = new int[arr.length()];

        for (int i = 0; i < arr.length(); i++) {
            result[i] = arr.getInt(i);
        }

        return new HotkeyModel(result);
    }
}
