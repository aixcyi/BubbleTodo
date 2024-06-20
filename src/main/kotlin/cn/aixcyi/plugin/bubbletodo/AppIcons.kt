package cn.aixcyi.plugin.bubbletodo

import com.intellij.icons.AllIcons
import com.intellij.ui.LayeredIcon
import javax.swing.Icon

/**
 * 项目内用到的合成图标，以及需要重命名来明晰含义的图标。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 * @see <a href="https://jetbrains.design/intellij/resources/icons_list/">Icon List - IntelliJ Platform UI Guidelines</a>
 */
object AppIcons {

    /**
     * 将多个 [Icon] 层叠成一个 [Icon]。
     */
    private fun zip(vararg icons: Icon): Icon {
        val icon = LayeredIcon(icons.size)
        for (i in icons.indices) {
            icon.setIcon(icons[i], i)
        }
        return icon
    }

    /**
     * 消息泡泡的图标。16x16
     *
     * @author <a href="https://github.com/aixcyi">砹小翼</a>
     */
    object Bubble {
        val Starred = AllIcons.Nodes.Favorite
        val Normal = AllIcons.General.BalloonInformation
        val Urgent = AllIcons.General.BalloonWarning
        val Important = AllIcons.General.BalloonError
        val UrgentAndImportant = AllIcons.General.InspectionsMixed
    }
}