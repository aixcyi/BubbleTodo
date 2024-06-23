package cn.aixcyi.plugin.bubbletodo.utils

import com.intellij.ui.dsl.builder.Cell
import org.jetbrains.annotations.ApiStatus
import javax.swing.JComponent

/**
 * 图形界面相关的工具函数。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
object MeowUiUtil {

    /**
     * 检测是否启用了 [NewUI](https://www.jetbrains.com/help/idea/new-ui.html) 。
     *
     * - 本方法适用于任意版本的 IntelliJ IDE。
     * - 232.5150 开始有 `com.intellij.ui.NewUI.isEnabled`。
     * - 213.2094 开始有 `com.intellij.ui.ExperimentalUI.isNewUI`，属于 [ApiStatus.Internal]。
     */
    fun isUsingNewUI(): Boolean = eval {
        // @since 232.5150.116
        // https://github.com/JetBrains/intellij-community/commit/ba6df8d944aa1f080d555223917c4b0aa7f43a26
        // return com.intellij.ui.NewUI.isEnabled()
        Class.forName("com.intellij.ui.NewUI").getMethod("isEnabled").invoke(null) as Boolean
    } ?: eval {
        // @since 213.2094
        // https://github.com/JetBrains/intellij-community/blob/213.2094/platform/platform-api/src/com/intellij/ui/ExperimentalUI.java
        // return com.intellij.ui.ExperimentalUI.isNewUI()
        Class.forName("com.intellij.ui.ExperimentalUI").getMethod("isNewUI").invoke(null) as Boolean
    } ?: false
}

/**
 * 让 [Cell] 横向填满当前的布局单元格。
 *
 * 该函数是为了同时兼容以下两种写法：
 *
 * - `align(com.intellij.ui.dsl.builder.AlignX.FILL)`
 * - `horizontalAlign(com.intellij.ui.dsl.gridLayout.HorizontalAlign.FILL)`
 */
fun <T : JComponent> Cell<T>.hFill(): Cell<T> {
    exec {
        val klass = Class.forName("com.intellij.ui.dsl.builder.Align")
        val param = Class.forName("com.intellij.ui.dsl.builder.AlignX")
            .kotlin.sealedSubclasses.first { it.simpleName == "FILL" }.objectInstance
        javaClass.getMethod("align", klass).invoke(this, param)
    }?.exec {
        val klass = Class.forName("com.intellij.ui.dsl.gridLayout.HorizontalAlign")
        val param = klass.enumConstants.map { it as Enum<*> }.first { it.name == "FILL" }
        javaClass.getMethod("horizontalAlign", klass).invoke(this, param)
    }
    return this
}

/**
 * 让 [Cell] 纵向填满当前的布局单元格。
 *
 * 该函数是为了同时兼容以下两种写法：
 *
 * - `align(com.intellij.ui.dsl.builder.AlignY.FILL)`
 * - `verticalAlign(com.intellij.ui.dsl.gridLayout.VerticalAlign.FILL)`
 */
fun <T : JComponent> Cell<T>.vFill(): Cell<T> {
    exec {
        val klass = Class.forName("com.intellij.ui.dsl.builder.Align")
        val param = Class.forName("com.intellij.ui.dsl.builder.AlignY")
            .kotlin.sealedSubclasses.first { it.simpleName == "FILL" }.objectInstance
        javaClass.getMethod("align", klass).invoke(this, param)
    }?.exec {
        val klass = Class.forName("com.intellij.ui.dsl.gridLayout.VerticalAlign")
        val param = klass.enumConstants.map { it as Enum<*> }.first { it.name == "FILL" }
        javaClass.getMethod("verticalAlign", klass).invoke(this, param)
    }
    return this
}