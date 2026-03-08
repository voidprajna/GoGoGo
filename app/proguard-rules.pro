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
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-keep class com.baidu.vi.** {*;}
-dontwarn com.baidu.**

# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Logto SDK
-keep class io.logto.sdk.** { *; }
-dontwarn io.logto.sdk.**

# Alipay SDK (Logto dependency)
-dontwarn com.alipay.sdk.**

# WeChat SDK (Logto dependency)
-dontwarn com.tencent.mm.opensdk.**

# Logback (Logto dependency)
-dontwarn ch.qos.logback.**
-dontwarn javax.management.**
-dontwarn javax.naming.**
-dontwarn javax.servlet.**
-dontwarn org.codehaus.janino.**
-dontwarn sun.reflect.**
