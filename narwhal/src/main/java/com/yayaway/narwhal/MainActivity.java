package com.yayaway.narwhal;

import android.app.Activity;
import android.os.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends Activity {

    static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
    }


    private String list = "{\"kind\":\"t3\",\"data\":{\"domain\":\"imgur.com\",\"banned_by\":null,\"media_embed\":{},\"subreddit\":\"pics\",\"selftext_html\":null,\"selftext\":\"\",\"likes\":null,\"suggested_sort\":null,\"user_reports\":[],\"secure_media\":null,\"link_flair_text\":\"\",\"id\":\"3uv7y7\",\"from_kind\":null,\"gilded\":0,\"archived\":false,\"clicked\":false,\"report_reasons\":null,\"author\":\"Proteon\",\"media\":null,\"score\":5836,\"approved_by\":null,\"over_18\":false,\"hidden\":false,\"preview\":{\"images\":[{\"source\":{\"url\":\"https://i.redditmedia.com/uABUT02BzGdF_2wDcmkBTk-c3PfLQwv45Bnf4pUnI8s.jpg?s=c3f5b60ae4b7fee4128f79c2dc4904c5\",\"width\":1653,\"height\":2480},\"resolutions\":[{\"url\":\"https://i.redditmedia.com/uABUT02BzGdF_2wDcmkBTk-c3PfLQwv45Bnf4pUnI8s.jpg?fit=crop&amp;crop=faces%2Centropy&amp;arh=2&amp;w=108&amp;s=bc66089c3c2a41860a4b3e71c09f7271\",\"width\":108,\"height\":162},{\"url\":\"https://i.redditmedia.com/uABUT02BzGdF_2wDcmkBTk-c3PfLQwv45Bnf4pUnI8s.jpg?fit=crop&amp;crop=faces%2Centropy&amp;arh=2&amp;w=216&amp;s=ade8f54951a5f9f77ea9f2062b838cb0\",\"width\":216,\"height\":324},{\"url\":\"https://i.redditmedia.com/uABUT02BzGdF_2wDcmkBTk-c3PfLQwv45Bnf4pUnI8s.jpg?fit=crop&amp;crop=faces%2Centropy&amp;arh=2&amp;w=320&amp;s=efd97be095f1fa0c7cc9ce2361978109\",\"width\":320,\"height\":480},{\"url\":\"https://i.redditmedia.com/uABUT02BzGdF_2wDcmkBTk-c3PfLQwv45Bnf4pUnI8s.jpg?fit=crop&amp;crop=faces%2Centropy&amp;arh=2&amp;w=640&amp;s=dba1b7cd269c6d28aff00fd3cdb48cb6\",\"width\":640,\"height\":960},{\"url\":\"https://i.redditmedia.com/uABUT02BzGdF_2wDcmkBTk-c3PfLQwv45Bnf4pUnI8s.jpg?fit=crop&amp;crop=faces%2Centropy&amp;arh=2&amp;w=960&amp;s=67c4c053246a14d7a99d813e0c6b2624\",\"width\":960,\"height\":1440},{\"url\":\"https://i.redditmedia.com/uABUT02BzGdF_2wDcmkBTk-c3PfLQwv45Bnf4pUnI8s.jpg?fit=crop&amp;crop=faces%2Centropy&amp;arh=2&amp;w=1080&amp;s=107e9f9ba42cec1f1a8f7298cfe8b4ba\",\"width\":1080,\"height\":1620}],\"variants\":{},\"id\":\"hI6K5awGarxim5qePITilsEQPamRJSVDlTWIa6afEwo\"}]},\"num_comments\":1709,\"thumbnail\":\"http://b.thumbs.redditmedia.com/SSEcJYefSZRpCI94k_ZoT7U7f44h--yW54X-zVFdZQs.jpg\",\"subreddit_id\":\"t5_2qh0u\",\"hide_score\":false,\"edited\":false,\"link_flair_css_class\":\"gilded\",\"author_flair_css_class\":null,\"downs\":0,\"secure_media_embed\":{},\"saved\":false,\"removal_reason\":null,\"post_hint\":\"image\",\"stickied\":false,\"from\":null,\"is_self\":false,\"from_id\":null,\"permalink\":\"/r/pics/comments/3uv7y7/hanging_fireplace/\",\"locked\":false,\"name\":\"t3_3uv7y7\",\"created\":1.44892893E9,\"url\":\"http://imgur.com/hFICy17.jpg\",\"author_flair_text\":null,\"quarantine\":false,\"title\":\"Hanging fireplace\",\"created_utc\":1.44890013E9,\"distinguished\":null,\"mod_reports\":[],\"visited\":false,\"num_reports\":null,\"ups\":5836}}";

}