package com.srujun.openhearthstone.json;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class JsonUtility {
    private static Json json = new Json();

    public static <T> T read(FileHandle file, Class<T> returnType) {
        return json.fromJson(returnType, file);
    }
}
