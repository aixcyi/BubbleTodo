package cn.aixcyi.plugin.bubbletodo.eitity

import cn.aixcyi.plugin.bubbletodo.AppIcons
import org.apache.commons.lang.RandomStringUtils
import java.time.ZonedDateTime

/**
 * 待办／便笺／备忘／提醒。
 *
 * Todo / Note / Memo / Notification
 *
 * @param id 标识符，用于全局标识一个泡泡。值应当由 [Bubble.randomUniqueID] 生成。
 * @param content 带格式的文本内容。
 * @param done 完成与否。
 * @param starred 是否被打上星标。
 * @param urgent 是否属于紧急事项。
 * @param important 是否属于重要事项。
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
data class Bubble(
    var id: String = randomUniqueID(),
    var content: String = "",
    var done: Boolean = false,
    var starred: Boolean = false,
    var urgent: Boolean = false,
    var important: Boolean = false,
) {
    companion object {
        /**
         * 生成一个随机自增的 ID。
         *
         * 它由时间戳和随机 Base36 字符串组成，中间使用小数点间隔。特性如下：
         *
         * 1. 每秒碰撞概率在 10 的 -32 到 -31 次方之间。
         * 2. 可以直观看到生成时间，以及方便手动排序。
         * 3. 由 Base36 字符集及字符 `"."` 组成，方便传输。
         * 4. `2001-09-09 01:46:40`（UTC+0）后输出 31 个字符。
         * 5. `2286-11-20 17:46:40`（UTC+0）后输出 32 个字符。
         *
         * @return 格式类似于 `1709138179.J86jaZcX9QnDsB7EJ9uS` 的字符串。
         */
        fun randomUniqueID(): String =
            ZonedDateTime.now().toEpochSecond().toString() + "." + RandomStringUtils.randomAlphanumeric(20)
    }

    /**
     * 获取能代表泡泡的图标。
     *
     * @param allowShowStarIcon 是否允许显示 “星标” 图标。
     */
    fun getIcon(allowShowStarIcon: Boolean = true) =
        if (allowShowStarIcon && this.starred)
            AppIcons.Bubble.Starred
        else if (this.urgent && this.important)
            AppIcons.Bubble.UrgentAndImportant
        else if (this.urgent)
            AppIcons.Bubble.Urgent
        else if (this.important)
            AppIcons.Bubble.Important
        else
            AppIcons.Bubble.Normal
}