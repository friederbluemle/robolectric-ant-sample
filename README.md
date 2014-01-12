# RobolectricAntSample [![Build Status](https://travis-ci.org/friederbluemle/RobolectricAntSample.png?branch=master)](https://travis-ci.org/friederbluemle/RobolectricAntSample) #

This project is based on [RobolectricSample](https://github.com/robolectric/RobolectricSample) and contains the same sample app and unit tests. The original project (RobolectricSample) uses the Maven build system/projects structure and Ant support was completely removed on Nov 05, 2013.

Some legacy apps cannot be easily migrated to a newer build system like Maven or
Gradle yet. If you are still using Ant as your main build system, and want to write unit tests using Robolectric, then RobolectricAntSample is for you. It demonstrates how to use Ant with Robolectric using a native Android Ant build project layout.

Tests can be run from both command line and Eclipse.

## Prerequisites and Setup ##

### For Ant ###

The Android SDK and a recent version of Ant should be all you need.

- The `ANDROID_HOME` environment variable must be set
- Running `android update project -p .` is optional

### For Eclipse ###

You need Eclipse with the Android ADT plugin set up. The test project uses a non-standard **Eclipse Classpath Variable** that is required
in order to resolve to path to android.jar.

- Go to **Eclipse Preferences**
- Open **Java** → **Build Path** → **Classpath Variables**
- Create a new variable called **ANDROID_HOME** pointing to your Android SDK installation folder
- Import RobolectricAntSample and the test project in the `test/` subdirectory as existing projects into your Eclipse workspace. Use General (not Android) on the import dialog.

## Running tests ##

### From command line ###

To build the main app and run unit tests:

    $ ant test

Test results (as txt and xml files) will be stored under `test/bin/reports/`

You can also run the tests and generate an HTML report:

    $ ant test-report

The HTML report can be found under `test/bin/reports/html/`

### From Eclipse ###

- Select **Run As** → **Run Configurations...** from the context menu of RobolectricAntSampleTest
- Create a new **JUnit** launch configuration
- Select **JUnit 4** as the test runner
- On the **Arguments** tab, under **Working directory**, select Other and choose RobolectricAntSample as the workspace
- Click **Choose one...** on the bottom to select a launcher and select **Eclipse JUnit Launcher**
- You can now run the unit tests by clicking **Run As** → **JUnit Test** in the context menu of the test project or test file

## Contributing ##

Contributions are welcome. Please fork and submit pull requests!

## License ##

MIT
