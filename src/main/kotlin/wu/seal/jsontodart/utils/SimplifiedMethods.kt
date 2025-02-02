package wu.seal.jsontodart.utils

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import wu.seal.jsontodart.ConfigManager

/**
 * File contains functions which simply other functions's invoke
 * Created by Seal.Wu on 2018/2/7.
 */


/**
 * do the action that could be roll-back
 */
fun executeCouldRollBackAction(project: Project?, action: (Project?) -> Unit) {
    CommandProcessor.getInstance().executeCommand(project, {
        ApplicationManager.getApplication().runWriteAction {
            action.invoke(project)
        }
    }, "insertKotlin", "JsonToKotlin")
}

/**
 * get the indent when generate kotlin class code
 */
fun getIndent(): String {

    return buildString {

        for (i in 1..ConfigManager.indent) {
            append(" ")
        }
    }
}

/**
 * get class string block list from a big string which contains classes
 */
fun getClassesStringList(classesString: String): List<String> {
    return classesString.split("\n\n").filter { it.isNotBlank() }
}

/**
 * export the class name from class block string
 */
fun getClassNameFromClassBlockString(classBlockString: String): String {
    return classBlockString.substringAfter("class").substringBefore("{").trim()
    //throw IllegalStateException("cannot find class name in classBlockString: $classBlockString")
}

fun showNotify(notifyMessage: String, project: Project?) {
    val notificationGroup = NotificationGroup("JSON to Dart Class", NotificationDisplayType.BALLOON, true)
    ApplicationManager.getApplication().invokeLater {
        val notification = notificationGroup.createNotification(notifyMessage, NotificationType.INFORMATION)
        Notifications.Bus.notify(notification, project)
    }
}

fun <E> List<E>.firstIndexAfterSpecificIndex(element: E, afterIndex: Int): Int {
    forEachIndexed { index, e ->
        if (e == element && index > afterIndex) {
            return index
        }
    }
    return -1
}

fun getCommentCode(comment: String): String {
    return comment.replace(Regex("[\n\r]"), "")
}
