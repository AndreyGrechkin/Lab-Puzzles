rootProject.name = "LabPuzzles"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven {
            url = uri("https://artifactory-external.vkpartner.ru/artifactory/maven")
        }
    }
}

include(":composeApp")
include(":uiKit")
include(":core:common")
include(":core:navigation")
include(":resources")
include(":data:database")
include(":data:store")

include(":features:onboarding")
include(":features:mainMenu")
include(":features:levelSelection")
include(":features:dailyChallenges")
include(":features:achievements")
include(":features:settings")
// :features:gameplay - ядро игрового процесса
include(":features:gameplay:common")      // общая игровая логика
include(":features:gameplay:waterSort")  // движок Water Sort
include(":features:gameplay:flowFree")   // движок Flow Free
include(":features:gameplay:sudoku")      // движок Судоку
include(":features:gameplay:sliding")     // движок Пятнашек
// :features:progress - система прогрессии
include(":features:progress:core")        // управление прогрессом
include(":features:progress:levelGenerator") // генератор уровней