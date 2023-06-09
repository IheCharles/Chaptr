package com.dev.textnet;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * Created by Dell on 2017/11/11.
 */

public class CustomImageSizeGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(CustomImageSizeModel.class, InputStream.class, new CustomImageSizeUrlLoaderFactory());

    }

    public interface CustomImageSizeModel {
        String requestCustomSizeUrl(int width, int height);
    }

    private class CustomImageSizeUrlLoaderFactory implements ModelLoaderFactory<CustomImageSizeModel, InputStream> {
        @Override
        public ModelLoader<CustomImageSizeModel, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new CustomImageSizeUrlLoader( context );
        }
        @Override
        public void teardown() {
        }
    }

    public class CustomImageSizeUrlLoader extends BaseGlideUrlLoader<CustomImageSizeModel> {
        public CustomImageSizeUrlLoader(Context context) {
            super(context);
        }

        @Override
        protected String getUrl(CustomImageSizeModel model, int width, int height) {
            return model.requestCustomSizeUrl(width, height);
        }
    }

    public static class CustomImageSizeModelFutureStudio implements CustomImageSizeModel {
        String baseImageUrl;

        public CustomImageSizeModelFutureStudio(String baseImageUrl) {
            this.baseImageUrl = baseImageUrl;
        }

        @Override
        public String requestCustomSizeUrl(int width, int height) {
            // previous way: we directly accessed the images
            // https://futurestud.io/images/logo.png

            // new way, server could handle additional parameter and provide the image in a specific size
            // in this case, the server would serve the image in 400x300 pixel size
            // https://futurestud.io/images/logo.png?w=400&h=300
            return baseImageUrl + "?w=" + width + "&h=" + height;
        }
    }
}
