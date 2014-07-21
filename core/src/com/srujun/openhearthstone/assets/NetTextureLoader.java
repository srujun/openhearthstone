package com.srujun.openhearthstone.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class NetTextureLoader extends AsynchronousAssetLoader<Texture, NetTextureLoader.NetTextureParameter> {
    Texture texture;

    public NetTextureLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, NetTextureParameter parameter) {

    }

    @Override
    public Texture loadSync(AssetManager manager, String fileName, FileHandle file, NetTextureParameter parameter) {
        return null;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, NetTextureParameter parameter) {
        return null;
    }

    static public class NetTextureParameter extends AssetLoaderParameters<Texture> {

    }
}
