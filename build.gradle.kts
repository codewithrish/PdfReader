// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false // Hilt plugin
    alias(libs.plugins.ksp) apply false // KSP plugin
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.firebase.app.distribution) apply false
}

tasks.register("createPackage") {
    doLast {
        // Get the package name input
        println("Enter package name in camel case (e.g., myPackage):")
        val input = readLine()?.trim()

        if (input.isNullOrEmpty() || !input.matches(Regex("^[a-z][a-zA-Z0-9]*$"))) {
            println("Invalid package name. Please enter a valid camel case name starting with a lowercase letter.")
            return@doLast
        }

        // Capitalize the first letter for class names
        val className = input.replaceFirstChar { it.uppercase() }
        val packagePath = "app/src/main/java/com/codewithrish/pdfreader/ui/screen/${input}"

        // Create the package directory
        val packageDir = File(project.projectDir, packagePath)
        if (packageDir.exists()) {
            println("Package already exists at $packagePath")
        } else {
            packageDir.mkdirs()
            println("Package created at $packagePath")

            val packageName = "package com.codewithrish.pdfreader.ui.screen.$input"
            // Create the files with base content
            val filesToCreate = listOf(
                "${className}ViewModel.kt" to """
                    $packageName
                    
                    import com.codewithrish.pdfreader.core.common.BaseViewModel
                    import dagger.hilt.android.lifecycle.HiltViewModel
                    import javax.inject.Inject
                    
                    @HiltViewModel
                    class ${className}ViewModel @Inject constructor(
                        analyticsProvider: AnalyticsProvider,
                    ) : BaseViewModel<${className}UiState, ${className}Event>(analyticsProvider) {
                        
                        override fun initState() = ${className}UiState()

                        override fun onEvent(event: ${className}Event) {

                        }
                        
                    }
                    
                """.trimIndent(),

                "${className}Screen.kt" to """
                    $packageName

                    import androidx.compose.runtime.Composable
                    import androidx.compose.ui.Modifier

                    @Composable
                    fun ${className}Screen(
                        modifier: Modifier = Modifier,
                        state: ${className}UiState = ${className}UiState(),
                        onEvent: (${className}Event) -> Unit,
                    ) {
                        
                    }
                    
                """.trimIndent(),

                "${className}UiState.kt" to """
                    $packageName

                    data class ${className}UiState(
                        val dummyData: String = "",
                    )
                    
                """.trimIndent(),

                "${className}Event.kt" to """
                    $packageName

                    sealed class ${className}Event {
                        
                    }
                    
                """.trimIndent()
            )

            filesToCreate.forEach { (fileName, content) ->
                val file = File(packageDir, fileName)
                file.writeText(content)
                println("File created: ${file.path}")
            }
        }
    }
}