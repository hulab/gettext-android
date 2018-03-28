# Gettext-Android
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [ ![Download](https://api.bintray.com/packages/nebneb/Gettext-Android/gettext-android/images/download.svg) ](https://bintray.com/nebneb/Gettext-Android/gettext-android/_latestVersion)

Gettext for Android allows you to use common po files to an Android project

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

Add gettext-android-1.0.jar to your `libs/` folder. Then, add the following to your `build.gradle` file:

```groovy
   repositories {
       jcenter()
   }
   
   dependencies {
       compile 'com.hulab.android:gettext-android:1.0.0'
   }
   
   apply plugin: 'gettext-android'

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