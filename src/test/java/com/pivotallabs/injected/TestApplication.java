package com.pivotallabs.injected;

import org.robolectric.Robolectric;
import org.robolectric.TestLifecycleApplication;
import roboguice.RoboGuice;

import java.lang.reflect.Method;

@SuppressWarnings("UnusedDeclaration")
public class TestApplication extends com.pivotallabs.injected.Application implements TestLifecycleApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(this), new RobolectricSampleModule());
    }

    @Override
    public void beforeTest(Method method) {
    }

    @Override
    public void prepareTest(Object test) {
        TestApplication application = (TestApplication) Robolectric.application;

        RoboGuice.setBaseApplicationInjector(application, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(application), new RobolectricSampleTestModule());

        RoboGuice.getInjector(application).injectMembers(test);
    }

    @Override
    public void afterTest(Method method) {
    }
}
