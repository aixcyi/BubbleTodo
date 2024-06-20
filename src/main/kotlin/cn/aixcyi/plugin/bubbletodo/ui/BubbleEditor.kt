package cn.aixcyi.plugin.bubbletodo.ui

import cn.aixcyi.plugin.bubbletodo.eitity.Bubble
import cn.aixcyi.plugin.bubbletodo.utils.IOUtil.message
import cn.aixcyi.plugin.bubbletodo.utils.hFill
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.LanguageTextField
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toMutableProperty
import java.awt.Font

class BubbleEditor(private val project: Project, bubble: Bubble?) : DialogWrapper(project, false) {

    val bubble = bubble ?: Bubble()

    init {
        title = if (bubble == null) message("dialog.BubbleCreator.title") else message("dialog.BubbleEditor.title")
        isResizable = true
        setOKButtonText(message("button.OK.text"))
        setCancelButtonText(message("button.Cancel.text"))
        super.init()
    }

    override fun createCenterPanel() = panel {
        row {
            cell(createDataArea())
                .bind(LanguageTextField::getText, LanguageTextField::setText, bubble::content.toMutableProperty())
                .hFill()
        }
        row {
            checkBox(message("checkbox.EditorDone")).bindSelected(bubble::done)
            checkBox(message("checkbox.EditorStarred")).bindSelected(bubble::starred)
            checkBox(message("checkbox.EditorUrgent")).bindSelected(bubble::urgent)
            checkBox(message("checkbox.EditorImportant")).bindSelected(bubble::important)
        }
    }

    private fun createDataArea(): LanguageTextField {
        val preferences = EditorColorsManager.getInstance().schemeForCurrentUITheme.fontPreferences
        val fontFamily = preferences.fontFamily
        val font = Font(fontFamily, Font.PLAIN, preferences.getSize(fontFamily))
        return LanguageTextField(HTMLLanguage.INSTANCE, project, "", false).apply {
            // setBorder(SideBorder(window.background, SideBorder.ALL))
            setBorder(null)
            setFont(font)
        }
    }
}