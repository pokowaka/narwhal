package com.yayaway.narwhal.reddit;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by erwinj on 12/3/15.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class DeviceIdentifierTest {

    private Context mContext;

    @Before
    public void initTargetContext() {
        mContext = getTargetContext();
        assertThat(mContext, notNullValue());
    }
    
    @Test
    public void verifyDeviceId() {
        UUID id = DeviceIdentifier.getDeviceId(mContext);
        assertThat(id, notNullValue());

        UUID id2 = DeviceIdentifier.getDeviceId(mContext);
        assertThat(id, is(id2));
    }
}