package cn.aixcyi.plugin.bubbletodo.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

// 主要模仿「通知栏」，其源自 IntelliJ IDEA 2022.1 EAP
// 相关文章 https://blog.jetbrains.com/idea/2022/01/intellij-idea-2022-1-eap-1/#new-notifications-tool-window
// 教程文档 https://plugins.jetbrains.com/docs/intellij/tool-windows.html

// 切换工具窗口的 AnAction 的 ID 为 Activate${toolWindow.id}ToolWindow
// 变量来自 plugin.xml
// 另可参阅 https://intellij-support.jetbrains.com/hc/en-us/community/posts/206127339-Adding-a-keyboard-shortcut-to-a-custom-toolwindow

/**
 * 泡泡清单工具窗口（的工厂类）。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
class BubblesToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, window: ToolWindow) {
        TodoContent(project, window)
    }
}