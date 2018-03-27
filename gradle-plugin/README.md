# Gettext-Android

Gettext for Android allows you to use common po files to an Android project

# Installation

First, put your po files at the root of your project, at the same level as your src/ folder.

```
    /my-project
    -- src/
    -- libs/
    -- po/
    ---- en.po
    ---- fr.po
    ---- ...

```

Add gettext-android-1.0.jar to your `libs/` folder. Then, add the following to your `build.gradle` file:

```groovy
    buildscript {
        repositories {
            // ...

            flatDir dirs: "libs"

            // ...
        }

        dependencies {
            // ...
            classpath 'com.hulab.gettext-android:gettext-android-1.0:1.0'
        }
    }

    dependencies {
        // ...

        compile files('libs/gettext-android-1.0.jar')

        // ...

        // Apply plugin to compile your *.po files
        apply plugin: 'gettext-android'
    }

```

# Usage
---
Once you have correctly added those lines to your build.gradle file, you can start using gettext:


---