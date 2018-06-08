cmake_minimum_required(VERSION 3.8)

set(CMAKE_MODULE_PATH ${CMAKE_CURRENT_LIST_DIR}/KotlinCMakeModule)

project(MagicWandServer Kotlin)

function(compile_macos)
    set(CMAKE_MACOSX_BUNDLE YES)

    set(MACOSX_BUNDLE_GUI_IDENTIFIER "org.augbari.MagicWand")
    set(MACOSX_BUNDLE_INFO_STRING "A Magic Wand to revolutionize Human Interaction")
    set(MACOSX_BUNDLE_ICON_FILE AppIcon.icns)
    set(MACOSX_BUNDLE_LONG_VERSION_STRING 1.0)
    set(MACOSX_BUNDLE_SHORT_VERSION_STRING 1.0)
    set(MACOSX_BUNDLE_BUNDLE_VERSION 1.0)
    set(MACOSX_BUNDLE_COPYRIGHT "AUG Bari")

    konanc_executable(
            NAME MagicWandServer
            SOURCES macOS/app.kt
    )

    add_custom_command(
            TARGET MagicWandServer
            POST_BUILD
            COMMAND ${CMAKE_COMMAND} -E copy ${CMAKE_BINARY_DIR}/MagicWandServer "${CMAKE_BINARY_DIR}/MagicWandServer.app/Contents/MacOS"
    )

    add_custom_command(
            TARGET MagicWandServer
            POST_BUILD
            COMMAND ${CMAKE_COMMAND} -E make_directory ${CMAKE_BINARY_DIR}/MagicWandServer.app/Contents/Resources
    )

    add_custom_command(
            TARGET MagicWandServer
            POST_BUILD
            COMMAND ${CMAKE_COMMAND} -E copy ${CMAKE_SOURCE_DIR}/assets/AppIcon.icns ${CMAKE_BINARY_DIR}/MagicWandServer.app/Contents/Resources
    )

    SET_SOURCE_FILES_PROPERTIES(
            MagicWandServer
            PROPERTIES
            MACOSX_PACKAGE_LOCATION MacOS
    )

    SET_SOURCE_FILES_PROPERTIES(
            assets/AppIcon.icns
            PROPERTIES
            MACOSX_PACKAGE_LOCATION Resources
    )
endfunction()

if (APPLE)
    compile_macos()
endif()