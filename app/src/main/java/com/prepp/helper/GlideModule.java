package com.prepp.helper;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;

/**
 * <p/>
 * Project: <b>blogmint-android</b><br/>
 * Created by: Anand K. Rai on 23/12/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class GlideModule implements com.bumptech.glide.module.GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, 2097152));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}