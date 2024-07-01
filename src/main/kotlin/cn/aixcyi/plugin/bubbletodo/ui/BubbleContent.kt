package cn.aixcyi.plugin.bubbletodo.ui

import cn.aixcyi.plugin.bubbletodo.entity.Bubble
import cn.aixcyi.plugin.bubbletodo.service.BubbleService
import cn.aixcyi.plugin.bubbletodo.service.impl.BubbleServiceImpl

import cn.aixcyi.plugin.bubbletodo.utils.BundleUtil.message
import cn.aixcyi.plugin.bubbletodo.utils.merge
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.ComponentUtil
import com.intellij.ui.components.JBOptionButton
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout
import java.awt.Component
import java.awt.event.ContainerAdapter
import java.awt.event.ContainerEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import javax.swing.ScrollPaneConstants

/**
 * 泡泡清单工具窗口（的图形界面）。
 *
 * @param project 工具窗口所在的项目。由 [BubblesToolWindowFactory] 提供。
 * @param toolWindow 工厂生产的半成品工具窗口。由 [BubblesToolWindowFactory] 提供。
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 * @see [com.intellij.notification.impl.NotificationContent]
 */
class BubbleContent(val project: Project, toolWindow: ToolWindow) {

    private val myBubbleList = JBPanelWithEmptyText(VerticalLayout(JBUI.scale(10)))
    private val myEventHandler = ComponentEventHandler()
    private var isScrollShown = false
    private var bubbleService = service<BubbleService>()

    init {
        // -------- 工具栏 --------
        val toolbar = ActionManager.getInstance().createActionToolbar(
            ActionPlaces.TOOLWINDOW_TOOLBAR_BAR, createToolbar(), true
        )

        // -------- 头部 --------
        // 工具栏+搜索栏
        val headPanel = JPanel(BorderLayout())
        headPanel.background = BubbleComponent.BG_DONE
        headPanel.add(toolbar.component, BorderLayout.NORTH)

        // -------- 主体 --------
        // 包含主体，仅限上下滚动
        val bodyPane = JBScrollPane(
            myBubbleList,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        )
        bodyPane.border = null
        val scroll = bodyPane.verticalScrollBar
        scroll.addAdjustmentListener {
            // 这个事件会被频繁触发，所以只在滚动条触发显示与隐藏状态切换的时候触发逻辑。
            val showing = scroll.isShowing()
            if (showing != isScrollShown) {
                isScrollShown = showing
                // 因为工具栏的图标不会填满工具栏的高度，所以列表的上边框需要窄一点，以便看起来跟其它边框等宽。
                // 列表的右侧因为滚动条占用了多一点的距离，所以也不必等宽。
                myBubbleList.border =
                    if (isScrollShown)
                        JBUI.Borders.empty(7, 10, 10, 5)
                    else
                        JBUI.Borders.empty(7, 10, 10, 10)
            }
        }

        // -------- 根面板 --------
        val rootPanel = JPanel(BorderLayout())
        rootPanel.background = BubbleComponent.BG_DONE
        rootPanel.add(headPanel, BorderLayout.NORTH)
        rootPanel.add(bodyPane, BorderLayout.CENTER)
        toolbar.targetComponent = rootPanel
        myEventHandler.add(rootPanel)

        // -------- 工厂开工 --------
        val content = ContentFactory.getInstance().createContent(rootPanel, "", false)
        content.preferredFocusableComponent = rootPanel
        val contentManager = toolWindow.contentManager
        contentManager.addContent(content)
        contentManager.setSelectedContent(content)
    }

    /**
     * 添加一个 [Bubble]，并创建相应的 [BubbleComponent]。
     *
     * - 如果 [BubbleComponent] 不符合搜索条件，那么会自动隐藏。
     * - 不要在循环中调用这个方法。因为它应当被优化。
     */
    fun add(bubble: Bubble) {
        bubbleService.save(bubble)
        val component = BubbleComponent(bubble)
        myBubbleList.add(component)
        myEventHandler.add(component)
        updateLayout()
    }

    /**
     * 枚举列表中的 [BubbleComponent] 并执行函数 [f]。
     */
    private fun iterateComponents(f: (BubbleComponent) -> Unit) {
        val count = myBubbleList.componentCount
        for (i in 0 until count) {
            f(myBubbleList.getComponent(i) as BubbleComponent)
        }
    }

    /**
     * 更新布局。因为 [myBubbleList] 并不能直接将 [BubbleComponent] 插入到顶部，故有此方法。
     */
    private fun updateLayout() {
        val layout = myBubbleList.layout
        iterateComponents { component: BubbleComponent? ->
            layout.removeLayoutComponent(component)
            layout.addLayoutComponent(null, component)
        }
    }

    private fun createToolbar(): DefaultActionGroup {
        val insertAction: DumbAwareAction =
            object : DumbAwareAction(message("action.AddBubble.text"), null, AllIcons.General.Add) {
                override fun actionPerformed(e: AnActionEvent) {
                    val editor = BubbleEditor(project, null)
                    if (editor.showAndGet()) {
                        add(editor.bubble)
                    }
                }
            }
        val group = DefaultActionGroup()
        group.add(insertAction.merge("Generate", myBubbleList))
        return group
    }
}

/**
 * 处理泡泡卡片内所有组件的消息事件。
 *
 * @see [com.intellij.notification.impl.ComponentEventHandler]
 */
internal class ComponentEventHandler {
    private var myHoverComponent: BubbleComponent? = null

    private val myMouseHandler: MouseAdapter = object : MouseAdapter() {
        override fun mouseMoved(e: MouseEvent) {
            if (myHoverComponent != null) {
                val component = ComponentUtil.getParentOfType(BubbleComponent::class.java, e.component)
                if (component != null) {
                    if (!component.hovering) {
                        component.hovering = true
                    }
                    myHoverComponent = component
                }
            }
        }

        override fun mouseExited(e: MouseEvent) {
            if (myHoverComponent != null) {
                val component = myHoverComponent!!
                if (component.hovering) {
                    component.hovering = false
                }
                myHoverComponent = null
            }
        }
    }

    fun add(component: Component) {
        adds(component) { c ->
            c.addMouseListener(myMouseHandler)
            c.addMouseMotionListener(myMouseHandler)
        }
    }

    private fun adds(component: Component, listener: (Component) -> Unit) {
        listener.invoke(component)

        if (component is JBOptionButton) {
            component.addContainerListener(object : ContainerAdapter() {
                override fun componentAdded(e: ContainerEvent) {
                    adds(e.child, listener)
                }
            })
        }

        for (child in UIUtil.uiChildren(component)) {
            adds(child, listener)
        }
    }
}