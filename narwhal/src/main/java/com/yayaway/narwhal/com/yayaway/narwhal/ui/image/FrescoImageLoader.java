package com.yayaway.narwhal.com.yayaway.narwhal.ui.image;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by erwinj on 12/9/15.
 */
public class FrescoImageLoader implements ImageLoader {
    public FrescoImageLoader(Fragment g) {
        Fresco.initialize(g.getContext());
    }

    @Override
    public void displayThumbnail(String url, ImageView view) {

    }

    private static final Logger logger = LoggerFactory.getLogger(FrescoImageLoader.class);

    @Override
    public void displayImage(String url, ImageView view, int width, int height) {
        if (url == null)
            return;
        if (url.endsWith("gifv")) {
            url = url.substring(0, url.length() - 1);
        } else if (!(url.endsWith("gif") || url.endsWith("jpg") || url.endsWith("png"))) {
            url += ".gif";
        }
        Uri uri = Uri.parse(url);
        if (uri == null)
            return;

        logger.info("displayImage() called with: " + "url = [" + url + "], view = [" + view + "]," +
                " width = [" + width + "], height = [" + height + "]");
        SimpleDraweeView mSimpleDraweeView = (SimpleDraweeView) view;
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        mSimpleDraweeView.setController(controller);
    }
}
