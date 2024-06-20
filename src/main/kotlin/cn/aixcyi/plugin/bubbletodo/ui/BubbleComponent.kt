package cn.aixcyi.plugin.bubbletodo.ui

import cn.aixcyi.plugin.bubbletodo.eitity.Bubble
import cn.aixcyi.plugin.bubbletodo.utils.MeowUiUtil.isUsingNewUI
import com.intellij.icons.AllIcons
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.notification.impl.ui.NotificationsUtil
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout
import java.awt.Desktop
import javax.swing.BorderFactory
import javax.swing.JEditorPane
import javax.swing.JPanel
import javax.swing.event.HyperlinkEvent

//  图标栏     内容栏     侧边栏
// ╭────┬──────────────┬────╮
// │    │              │    │
// │    │              │    │
// │    │              │    │
// │    │              │    │
// ╰────┴──────────────┴────╯
//  icon      data      side

/**
 * 泡泡卡片（图形界面）。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 * @see [com.intellij.notification.impl.NotificationComponent]
 */
class BubbleComponent(val bubble: Bubble) : JBPanel<BubbleComponent>() {

    companion object {
        /**
         * 未完成的泡泡的背景颜色。
         *
         * @see [com.intellij.notification.impl.NotificationComponent.BG_COLOR]
         */
        val BG_DOING = JBColor.namedColor(
            "NotificationsToolwindow.newNotification.background",
            JBColor(0xE6EEF7, 0x45494A)
        )

        /**
         * 已完成的泡泡的背景颜色，与泡泡列表背景色相同。
         *
         * @see [com.intellij.notification.impl.NotificationComponent.NEW_DEFAULT_COLOR]
         */
        val BG_DONE: JBColor
            get() = if (isUsingNewUI())
                JBUI.CurrentTheme.ToolWindow.background() as JBColor
            else
                UIUtil.getListBackground() as JBColor
    }

    private var myIconLabel = JBLabel()
    private var onHoverState = false
    private var myRoundColor = BG_DOING
    private val myDataArea = JEditorPane()  // 卡片中间的内容，占据卡片大部分空间。用编辑器是为了渲染HTML以及允许选择复制。
    private val myMorePresentation = Presentation()
    private val myMoreButton = ActionButton(  // 卡片右上角的“更多”按钮
        createMoreMenu(),
        myMorePresentation,
        ActionPlaces.UNKNOWN,
        ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE
    )

    /**
     * 设置泡泡卡片的热点状态。`true` 表示鼠标（光标）在当前卡片组件的矩形范围内。
     */
    var hovering: Boolean
        get() = onHoverState
        set(state) {
            onHoverState = state
            myMoreButton.isVisible = state
        }

    // 图标栏（左边）
    init {
        val iconPanel = JPanel(VerticalLayout(3))
        add(iconPanel, BorderLayout.WEST)
        iconPanel.isOpaque = false
        iconPanel.add(myIconLabel, VerticalLayout.TOP)
    }

    // 内容栏（中间）
    // 顶部的空白边框用来让第一行文本与左侧的图标按钮对齐
    init {
        val dataPanel = JPanel(VerticalLayout(JBUI.scale(3)))
        add(dataPanel, BorderLayout.CENTER)
        dataPanel.isOpaque = false
        dataPanel.border = BorderFactory.createEmptyBorder(3, 0, 0, 0)
        dataPanel.add(myDataArea, BorderLayout.CENTER)
        myDataArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, java.lang.Boolean.TRUE)
        myDataArea.isEditable = false
        myDataArea.isOpaque = false
        myDataArea.border = null
        myDataArea.contentType = "text/html"
        myDataArea.addHyperlinkListener { e: HyperlinkEvent ->
            if (e.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.url.toURI())
                    } catch (_: Exception) {
                    }
                }
            }
        }
        NotificationsUtil.configureHtmlEditorKit(myDataArea, false)
    }

    // 侧边栏（右边）
    init {
        val sidePanel = JPanel(VerticalLayout(3))
        add(sidePanel, BorderLayout.EAST)
        sidePanel.isOpaque = false
        sidePanel.add(myMoreButton, VerticalLayout.TOP)
        myMorePresentation.hoveredIcon = AllIcons.Actions.More
        myMorePresentation.putClientProperty(ActionButton.HIDE_DROPDOWN_ICON, java.lang.Boolean.TRUE)
    }

    init {
        layout = BorderLayout(JBUI.scale(5), 0)
        border = JBUI.Borders.empty(10)
        isOpaque = true
        background = BG_DONE
        update()
    }

    /**
     * 创建 “┄” 按钮的菜单。
     */
    private fun createMoreMenu(): DefaultActionGroup {
        val group = DefaultActionGroup()
        group.isPopup = true
        return group
    }

    /**
     * 用于在编辑泡泡内容后，更新泡泡卡片。
     */
    private fun update() {
        // 图标栏
        myIconLabel.icon = bubble.getIcon()

        // 内容栏
        val fontStyle = NotificationsUtil.getFontStyle()
        val text = StringUtil.replace(bubble.content, "<p/>", "<br>")
        val code = NotificationsUtil.buildHtml(null, null, text, null, null, null, fontStyle)
        myDataArea.text = code
        NotificationsManagerImpl.setTextAccessibleName(myDataArea, text)

        // 整体
        myRoundColor = if (bubble.done) BG_DONE else BG_DOING
        repaint()
    }
}