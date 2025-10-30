pluginManagement {
    repositories {
        // 👇 A FORMA CORRETA E SIMPLES 👇
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// O seu bloco 'dependencyResolutionManagement' já estava PERFEITO.
// Deixe-o exatamente como está.
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Challenge - WTC"
include(":app")