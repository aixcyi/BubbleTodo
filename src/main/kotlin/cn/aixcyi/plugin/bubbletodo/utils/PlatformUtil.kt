package cn.aixcyi.plugin.bubbletodo.utils

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.DumbAwareAction
import javax.swing.JComponent

/**
 * 平台相关的兼容函数和定制工具。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
object PlatformUtil {
    val LOGGER = thisLogger()
}

/**
 * 将源 [AnAction] 的快捷键复制到自身。
 *
 * 用于锚定快捷键，也就是在终端用户改过快捷键的情况下，同步相应的快捷键。
 *
 * @param sourceActionId 事件ID。事件不存在时不会报错，也不会复制快捷键。
 * @param component 快捷键绑定到哪个组件。
 * @return 目标 [AnAction] 自身。
 * @see [com.intellij.openapi.actionSystem.IdeActions]
 */
fun DumbAwareAction.merge(sourceActionId: String, component: JComponent?): DumbAwareAction {
    ActionManager.getInstance().getAction(sourceActionId)?.apply {
        copyShortcutFrom(this)
        registerCustomShortcutSet(this.shortcutSet, component)
    } ?: run {
        PlatformUtil.LOGGER.debug("source action $sourceActionId does not exist.")
    }
    return this
}