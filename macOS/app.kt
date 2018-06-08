import kotlinx.cinterop.*
import platform.AppKit.*
import platform.Foundation.*
import platform.osx.*
import platform.CoreGraphics.*
import platform.CoreFoundation.*

/*
 * TODO: MqttKotlinAPI for Kotlin/Native.
 * TODO: Sense for left/right arrow command and apply it.
 */

/**
 * Initialize app by giving the proper AppDelegate to NSApplication.
 */
private fun runApp() {
    val app = NSApplication.sharedApplication()
    app.delegate = AppDelegate()
    app.setActivationPolicy(NSApplicationActivationPolicy.NSApplicationActivationPolicyAccessory)
    app.activateIgnoringOtherApps(true)
    app.run()
}

/**
 * Application implementation following Apple programming guidelines.
 */
private class AppDelegate() : NSObject(), NSApplicationDelegateProtocol {
    private var item: NSStatusItem

    /**
     * Main constructor.
     *
     * This is a StatusBar-based Application, so it is not needed a NSWindow or
     * something else. Just show the icon on System Menu Bar.
     */
    init {
        var menu = NSMenu()
        item = NSStatusBar.systemStatusBar.statusItemWithLength(NSVariableStatusItemLength)

        item.title = "MagicWand"
        menu.addItem(NSMenuItem("Send Key Event", NSSelectorFromString("synthesizeKeyboardEvent"), ""))
        menu.addItem(NSMenuItem("Quit", NSSelectorFromString("quit"), ""))

        item.menu = menu
    }

    /**
     * Callback to Synthesize Keyboard Event. Action for Menu Bar button.
     */
    @ObjCAction
    fun synthesizeKeyboardEvent() {
        testKeyboardEvent()
    }

    /**
     * Callback to quit the application with proper NSApplication method.
     */
    @ObjCAction
    fun quit() {
        NSApplication.sharedApplication().stop(this)
    }

    /**
     * Use ObjC CoreGraphics framework to simulate COMMAND + SPACE insertion.
     * Should activate Spotlight Search Bar.
     */
    fun testKeyboardEvent() {
        val src = CGEventSourceCreate(kCGEventSourceStateHIDSystemState)
        val command_down = CGEventCreateKeyboardEvent(src, 0x38, true)
        val command_up = CGEventCreateKeyboardEvent(src, 0x38, false)
        val space_down =  CGEventCreateKeyboardEvent(src, 0x31, true)
        val space_up = CGEventCreateKeyboardEvent(src, 0x31, false)

        CGEventSetFlags(space_down, kCGEventFlagMaskCommand)
        CGEventPost(kCGSessionEventTap, command_down)
        CGEventPost(kCGSessionEventTap, space_down)
        CGEventPost(kCGSessionEventTap, space_up)
        CGEventPost(kCGSessionEventTap, command_up)

        CFRelease(command_down)
        CFRelease(command_up)
        CFRelease(space_down)
        CFRelease(space_up)
    }
}

/**
 * Main entry for the program.
 * Just run NSApplication as soon as possible with ObjC garbage
 * collector.
 */
fun main(args: Array<String>) {
    autoreleasepool {
        runApp()
    }
}