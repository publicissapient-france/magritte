# for model classes
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-keep class fr.xebia.magritte.model.** {*;}
#-keepclassmembers class fr.xebia.magritte.model.** {*;}
#-keepnames @kotlin.Metadata class fr.xebia.magritte.model.**

-keepattributes Signature
-keepattributes *Annotation*
-keepattributes Exceptions

# square common
-dontwarn okio.**
-dontwarn javax.annotation.**

# okhttp
-dontwarn okhttp3.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Retrofit
# Retain service method parameters.
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# moshi
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *

# tensorflow
-keep class org.tensorflow.** { *; }
-dontwarn org.tensorflow.lite.*