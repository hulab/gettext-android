# Gettext-Android
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [ ![Download](https://api.bintray.com/packages/nebneb/Gettext-Android/gettext/images/download.svg) ](https://bintray.com/nebneb/Gettext-Android/gettext/_latestVersion)

Gettext for Android allows you to use common po files to an Android project.

# Installation

First, put your po files at the root of your project, at the same level as your src/ folder.

``` 
    my-project/
         ├── src/
         ├── po/
         │  ├── en.po
         │  ├── en_US.po
         │  ├── fr.po
         │  ├── fr_CH.po
         │  ├── ru.po
         │  └ ... 
         └ ...
```

Then, add the following to your `build.gradle` file:

```groovy
    buildscript {
        repositories {
            jcenter()
        } 
       
        dependencies {
            // Classpath dependency for gettext gradle plugin
            classpath "gradle.plugin.com.hulab.gradle:gradle-plugin:1.0.0"
        }
    }
   
   
   dependencies {
   
        // Gettext library dependency
        implementation 'com.hulab.android:gettext:1.0.1'
   }
   
   // Apply plugin
   apply plugin: "com.hulab.gradle.gettext-android"

```

# Usage
---
Once you have correctly added those lines to your build.gradle file, you can start using gettext:

``` java
public class MainActivity extends AppCompatActivity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize PO with your context to get the device's locale. 
        // You can also use a specific locale by using PO.setLocale(Context context, String locale).
        
        PO.setLocale(this);
 
        // Use simple translated string with PO.gettext(String msgId)
         
        ((TextView) findViewById(R.id.apples)).setText(PO.gettext("Choose number of apples"));
        ((TextView) findViewById(R.id.exclu)).setText(PO.gettext("Exclu fr"));
         
        ((SeekBar) findViewById(R.id.seek_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            
                // Or use plurals with PO.ngettext(String msgId, String msgIdPlural, Integer number)
                ((TextView) findViewById(R.id.x_apples)).setText(PO.ngettext("%d apple", "%d apples", progress));
                
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}

```


---


# About this library

Localization on Android is convenient enough for your app as long as you don't need to use same translations for an iOS app.
As you might know, iOS localization works with different files, different injections characters, and different keys system.

At Mapstr, we have been undergoing with this issue and every new translation was a pain to add and use. We needed to translate,
have it in the right format for both platforms, hence manage a double keys / value, and of course trying to avoid duplicated 
and missing strings.

For this reason we thought about having a true multi-platform tool that would allow us to have a same translation base, in 
a way that iOS and Android could use it. Keeping goals in mind, our researches pinpointed the .po format, used by many 
softwares and supported by many translations services. Moreover, it manages plurals better than the regular .xml files, 
as plural rules are declared directly in the .po file.  

The challenge on Android was to have a simple enough system that would give the developer the opportunity to simply integrate
his .po translation files dependencies. 

At Gradle Sync time, the plugin considers every module in the current project tree and seek for a `po/ folder at root. `
For every found module, the plugin will download and run a python script to generate java classes with those po files under 
the `build/generated/sources/translations/` folder, and add it as a source set to the project. One class is generated for 
each .po file under the source `po/` folder.
  
Those classes will be processed by your build script, and stay available at runtime, Gettext library will be able to reach 
out the right translation class depending on the locale found in the initializing context. You can obviously initialize 
Gettext with a specific locale.
 


# Next improvements

This plugin and library are a usable proof of concept. It is convenient enough for your translation flow, but is far from
xml usability in android development. You don't have any auto-completion, so it's easy to make a mistake when using Gettext.
For this reason, one of the next improvements will be to create a Android Studio plugin to make those strings recognized by 
your development environment, and have auto-completion back in the game.

Obviously, as this lib and plugin are open-source, I'm more than open to suggestions, and would be happy to make and see this 
cross-platform ambition becoming a reference for building localized apps. 



# Credits

Many thanks to Aurélien Gâteau who wrote the scripts to generate java classes from PO files.