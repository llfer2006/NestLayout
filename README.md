# NestLayout for Android
Nested scroll section
This project aims to provide a nest scrolled widget for Android,It was originally based on support-v4 library.      

![Screenshot](https://github.com/llfer2006/NestLayout/blob/master/images/23.gif)

##Feature
 * Support Nest scroll widget
 * Support Android version 2.3+
 * Current work width:
      * **RecycleView**
      * **NestScrollView**
      * **NestWebView**    


Repository at <https://github.com/llfer2006/NestLayout>.
 
##Usage

###Layout
``` xml
<com.llf.nestlayout.library.NestLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent" android:layout_height="match_parent"
    android:orientation="vertical">
    <LinearLayout android:layout_width="match_parent" android:layout_height="match_parent"
        android:orientation="vertical" app:section="true">
        <TextView android:layout_width="match_parent" android:layout_height="48dp"
            android:background="#6600FF00" android:text="RecycleView"
            android:gravity="center_vertical" />
        <android.support.v7.widget.RecyclerView android:layout_width="match_parent"
            android:layout_height="0dp" android:layout_weight="1"
            android:id="@+id/listview"/>
    </LinearLayout>
    <TextView android:layout_width="match_parent" android:layout_height="48dp"
        android:background="#6600FF00" android:text="ScrollView" app:section="true"
        android:gravity="center_vertical"/>
    <android.support.v4.widget.NestedScrollView android:layout_width="match_parent"
        android:layout_height="match_parent" android:fillViewport="true">
        <LinearLayout android:layout_width="match_parent" android:layout_height="match_parent"
            android:orientation="vertical">
            <ImageView android:layout_width="match_parent" android:layout_height="match_parent"
                android:src="@drawable/bgs" android:scaleType="center"/>
        </LinearLayout>
    </android.support.v4.widget.NestedScrollView>
    <TextView android:layout_width="match_parent" android:layout_height="48dp"
        android:background="#6600FF00" android:text="WebView" app:section="true"
        android:gravity="center_vertical"/>
    <com.llf.nestlayout.library.NestWebView android:id="@+id/webview"
        android:layout_width="match_parent" android:layout_height="match_parent"/>
</com.llf.nestlayout.library.NestLayout>
```
