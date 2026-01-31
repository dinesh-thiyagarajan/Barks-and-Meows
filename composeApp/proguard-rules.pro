# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Hide the original source file name
-renamesourcefileattribute SourceFile

# ===== Kotlin Metadata (Required for Reflection) =====
-keep class kotlin.Metadata { *; }

# ===== Jetpack Compose (R8 handles most automatically) =====
# Only keep what's needed for runtime composition
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# ===== Firebase =====
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Keep Firebase Firestore model classes
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName *;
}

# Firebase Auth
-keep class com.google.firebase.auth.** { *; }

# ===== Koin DI (Only keep what's needed for reflection) =====
# Keep Koin module definitions
-keepclassmembers class * {
    public <init>(...);
}
# Keep ViewModels and UseCases for Koin injection
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keep class com.app.**.useCases.** { *; }

# ===== Kotlin Serialization =====
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep Serializer classes
-keep,includedescriptorclasses class com.app.**$$serializer { *; }
-keepclassmembers class com.app.** {
    *** Companion;
}
-keepclasseswithmembers class com.app.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ===== ViewModel (Already covered in Koin section) =====
# R8 handles Navigation and Lifecycle automatically

# ===== Your App Classes =====
# Keep data models used with Firebase Firestore (needs reflection)
-keep class com.app.**.models.** { *; }
-keep class com.app.**.entities.** { *; }

# Keep ViewModels (for Koin injection)
-keep class com.app.**.viewModels.** { *; }

# ===== Reflection & Annotations =====
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes Exceptions

# ===== Parcelable =====
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

# ===== Enum =====
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ===== Remove Logging in Release =====
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# ===== WorkManager (Only keep Worker classes) =====
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.CoroutineWorker

# ===== Common Android =====
-keep class * extends android.app.Application
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# ===== Optimization Settings =====
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify
