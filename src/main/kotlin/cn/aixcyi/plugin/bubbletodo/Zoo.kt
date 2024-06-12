package cn.aixcyi.plugin.bubbletodo

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey
import java.util.*

object Zoo {

    // 翻译包
    private val BUNDLE: ResourceBundle =
        ResourceBundle.getBundle("messages.BubbleTodoBundle", DynamicBundle.getLocale())

    /**
     * 获取本地化翻译。
     *
     * A utility function provides translated messages.
     *
     * @param key properties 文件中的键。
     * @return properties 文件中键对应的值。
     */
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = "messages.BubbleTodoBundle") key: String): String {
        return BUNDLE.getString(key)
    }
}