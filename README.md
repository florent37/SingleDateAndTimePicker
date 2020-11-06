# SingleDateAndTimePicker

You can now select a date and a time with only one widget !


<a href="https://goo.gl/WXW8Dc">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>



[![screen](https://raw.githubusercontent.com/florent37/SingleDateAndTimePicker/master/media/new_video.gif)](https://www.github.com/florent37/SingleDateAndTimePicker)

# Usage

```java
new SingleDateAndTimePickerDialog.Builder(context)
            //.bottomSheet()
            //.curved()
            //.stepSizeMinutes(15)
            //.displayHours(false)
            //.displayMinutes(false)
            //.todayText("aujourd'hui")
            .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                @Override
                public void onDisplayed(SingleDateAndTimePicker picker) {
                    // Retrieve the SingleDateAndTimePicker
                }
                                
                @Override
                public void onClosed(SingleDateAndTimePicker picker) {
                    // On dialog closed 
                }
            })
            .title("Simple")
            .listener(new SingleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(Date date) {
                    
                }
            }).display();
```

## Select 2 dates

[![screen](https://raw.githubusercontent.com/florent37/SingleDateAndTimePicker/master/media/double_small_crop.png)](https://www.github.com/florent37/SingleDateAndTimePicker)

```java
new DoubleDateAndTimePickerDialog.Builder(context)
            //.bottomSheet()
            //.curved()
            //.stepSizeMinutes(15)
            .title("Double")
            .tab0Text("Depart")
            .tab1Text("Return")
            .listener(new DoubleDateAndTimePickerDialog.Listener() {
                @Override
                public void onDateSelected(List<Date> dates) {
                
                }
        }).display();
```

## Display days, months and years

[![screen](https://raw.githubusercontent.com/florent37/SingleDateAndTimePicker/master/media/years_crop.png)](https://www.github.com/florent37/SingleDateAndTimePicker)

```java
new SingleDateAndTimePickerDialog.Builder(this)
            .bottomSheet()
            .curved()
            .displayMinutes(false)
            .displayHours(false)
            .displayDays(false)
            .displayMonth(true)
            .displayYears(true)
            .displayDaysOfMonth(true)
            .display();
```

## Include in a layout

[![screen](https://raw.githubusercontent.com/florent37/SingleDateAndTimePicker/master/media/layout_small.png)](https://www.github.com/florent37/SingleDateAndTimePicker)

```xml
<com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
        android:layout_width="wrap_content"
        android:layout_height="230dp"
        app:picker_curved="true"
        app:picker_cyclic="true"
        app:picker_visibleItemCount="7"
        />
```

# iOS like :P

[![screen](https://raw.githubusercontent.com/florent37/SingleDateAndTimePicker/master/media/ios_simple_crop.png)](https://www.github.com/florent37/SingleDateAndTimePicker)

```java
new SingleDateAndTimePickerDialog.Builder(context)
                                    .bottomSheet()
                                    .curved()
```

[![screen](https://raw.githubusercontent.com/florent37/SingleDateAndTimePicker/master/media/ios_double_crop.png)](https://www.github.com/florent37/SingleDateAndTimePicker)

```java
new DoubleDateAndTimePickerDialog.Builder(context)
                                    .bottomSheet()
                                    .curved()
```

# Customisation

You can change the minutes steps (default : 5min)
```java
new SingleDateAndTimePickerDialog.Builder(context)
            .stepSizeMinutes(15)
            .display();
```

And change some colors

[![screen](https://raw.githubusercontent.com/florent37/SingleDateAndTimePicker/master/media/custom_colors.png)](https://www.github.com/florent37/SingleDateAndTimePicker)

```java
new SingleDateAndTimePickerDialog.Builder(context)
            .backgroundColor(Color.BLACK)
            .mainColor(Color.GREEN)
            .titleColor(Color.WHITE)
            .display();
```

# Date range

Require user to select a date between a range

```java
new SingleDateAndTimePickerDialog.Builder(context)
            .defaultDate(defaultDate)
            .minDateRange(minDate)
            .maxDateRange(maxDate)
            .display();
```

Or simply require user to select a future date

```java
new SingleDateAndTimePickerDialog.Builder(context)
            .mustBeOnFuture()
            .display();
```

# Changing typeface

```java
final SingleDateAndTimePicker singleDateAndTimePicker2 = findViewById(R.id.single_day_picker2);
singleDateAndTimePicker2.setTypeface(ResourcesCompat.getFont(this, R.font.dinot_regular));
```

Or pass it as an attribute in the XML layout. (See XML section on how to use it.)

# XML

Some/most options are also available via XML:

```
    <com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
        android:id="@+id/single_day_picker"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:picker_itemSpacing="6dp"
        app:picker_curvedMaxAngle="45"
        app:picker_curved="true"
        app:picker_selectorColor="@android:color/transparent"
        app:picker_stepSizeHours="2"
        app:picker_stepSizeMinutes="5"
        app:picker_cyclic="false"        
        app:picker_dayCount="31"
        app:picker_mustBeOnFuture="true"
        app:picker_visibleItemCount="7"
        app:fontFamily="@font/dinot_bold"
        />
```

* picker_itemSpacing: Margin between items. Only has effect with
  height=wrap-content
* picker_curvedMaxAngle sets the max angle of top/bottom items. If 45
  then the visible 'window' of the wheel is a 'quarter' of the circle.
  If 90 (default) its rolling on a half-circle
* `app:fontFamily` or `android:fontFamily` sets the typeface/font to be
  used with the date picker.
  Note - For api below v-16 use `app:fontFamily`

Get divider lines around selected by overwriting one or more of
```
    <color name="picker_default_divider_color">@android:color/transparent</color>
    <dimen name="picker_default_divider_height">1dp</dimen>
    <drawable name="picker_default_divider">@drawable/picker_divider</drawable>
```
Use in conjuction with
`app:picker_selectorColor="@android:color/transparent"` on layout.

# Download

<a href='https://ko-fi.com/A160LCC' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=0' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

In your module [![Download](https://api.bintray.com/packages/florent37/maven/SingleDateAndTimePicker/images/download.svg)](https://bintray.com/florent37/maven/SingleDateAndTimePicker/_latestVersion)
```groovy
implementation 'com.github.florent37:singledateandtimepicker:2.2.7'
//compatible with androidX
```

# Credits

Author: Florent Champigny

Blog : [http://www.tutos-android-france.com/](http://www.tutos-android-france.com/)

Fiches Plateau Moto : [https://www.fiches-plateau-moto.fr/](https://www.fiches-plateau-moto.fr/)

<a href="https://goo.gl/WXW8Dc">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>


<a href="https://plus.google.com/+florentchampigny">
  <img alt="Follow me on Google+"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/gplus.png" />
</a>
<a href="https://twitter.com/florent_champ">
  <img alt="Follow me on Twitter"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/twitter.png" />
</a>
<a href="https://www.linkedin.com/in/florentchampigny">
  <img alt="Follow me on LinkedIn"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/linkedin.png" />
</a>


License
--------

    Copyright 2016 florent37, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
