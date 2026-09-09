package client

@RequiresOptIn(message = "This API is still unfinished and is not meant to be used. Anything can change.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class GDClientApi()
