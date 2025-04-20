
## Hummer
-keep @interface com.didi.hummer.annotation.*
-keep @com.didi.hummer.annotation.Component class * {*;}
-keep @com.didi.hummer.annotation.Module class * {*;}
-keep class com.didi.hummer.core.engine.jsc.jni.** {*;}
-keep class com.didi.hummer.core.engine.napi.** {*;}
-keep class com.didi.hummer.core.exception.JSException {*;}
-keep class com.didi.hummer.render.component.anim.AnimViewWrapper {*;}
-keep class com.facebook.yoga.** {*;}
-keep class com.facebook.jni.** {*;}