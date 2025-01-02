# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# General Proguard settings
-dontwarn okhttp3.**
-dontwarn org.conscrypt.**
-ignorewarnings

# Keep Firebase classes
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes Signature

# Keep classes required for Gson serialization/deserialization
-keep class com.google.gson.** { *; }
-keep class com.google.gson.annotations.** { *; }

# Keep Hilt-related classes
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keepclassmembers,allowobfuscation interface * {
    @dagger.* <methods>;
}
-dontwarn dagger.hilt.internal.**
-keepnames @dagger.hilt.** class *
#-keepclassmembers class ** {
#    @dagger.hilt.android.internal.lifecycle.HiltViewModel *;
#}

# Keep Room database models and generated code
-keep class androidx.room.** { *; }
-keep @androidx.room.* class * { *; }
-dontwarn androidx.room.paging.**

# Keep kotlinx.serialization classes
-keepclassmembers class kotlinx.serialization.** { *; }
-keepclasseswithmembers class kotlinx.serialization.Serializable { *; }

# Keep Timber logging
-assumenosideeffects class timber.log.Timber {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# Keep Lottie classes
-keep class com.airbnb.lottie.** { *; }

# Keep PDF Viewer and Box classes
-keep class com.tom_roush.pdfbox.** { *; }
-keep class com.tom_roush.fontbox.** { *; }

# Keep MixPanel classes
-keep class com.mixpanel.android.** { *; }

# Keep Joda-Time classes
-keep class org.joda.time.** { *; }

# Keep Datastore classes
-keep class androidx.datastore.** { *; }

# Keep Coil image library
-keep class coil.** { *; }
-dontwarn coil.**

# Keep Protobuf generated classes
-keep class com.google.protobuf.** { *; }
-dontwarn com.google.protobuf.**

# Keep Compose-related classes
-keep class androidx.compose.** { *; }
-keep @androidx.compose.runtime.Composable class * { *; }
-keepclassmembers class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Exclude generated Proto files
-dontwarn google.protobuf.**

# Keep SplashScreen API classes
-keep class androidx.core.splashscreen.** { *; }

# Keep permissions-related classes
-keep class com.google.accompanist.permissions.** { *; }

# Keep Adaptive Navigation and Layout classes
-keep class androidx.compose.material3.** { *; }

# Keep Kotlin reflection metadata
-keepclassmembers class ** {
    @kotlin.Metadata *;
}

# Exclude test classes
-dontwarn org.junit.**
-dontwarn androidx.test.**

# Enable shrinking, obfuscation, and optimization
-optimizations !method/inlining/*

# Ensure AppDistribution files are preserved
-keep class com.google.firebase.appdistribution.** { *; }

# Preserve Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
