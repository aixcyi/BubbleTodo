package cn.aixcyi.plugin.bubbletodo.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.JPanel

/**
 * 泡泡清单工具窗口（的图形界面）。
 *
 * @param project 工具窗口所在的项目。由 [BubblesToolWindowFactory] 提供。
 * @param toolWindow 工厂生产的半成品工具窗口。由 [BubblesToolWindowFactory] 提供。
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 * @see [com.intellij.notification.impl.NotificationContent]
 */
class TodoContent(val project: Project, toolWindow: ToolWindow) {
    init {
        // -------- 根面板 --------
        val rootPanel = JPanel(BorderLayout())

        // -------- 工厂开工 --------
        val content = ContentFactory.getInstance().createContent(rootPanel, "", false)
        content.preferredFocusableComponent = rootPanel
        val contentManager = toolWindow.contentManager
        contentManager.addContent(content)
        contentManager.setSelectedContent(content)
    }
}