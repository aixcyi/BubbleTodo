package cn.aixcyi.plugin.bubbletodo.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.shelf.ShelveChangesManager
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import java.io.InputStream


class SQLOperation : ShelveChangesManager.PostStartupActivity() {

    companion object {
        lateinit var sqlSessionFactory: SqlSessionFactory
    }


    override fun runActivity(project: Project) {
        super.runActivity(project)
        sqlSessionFactory = SqlSessionFactoryBuilder().build(acquireMybatisConfig())

    }


    private fun acquireMybatisConfig(): InputStream {
        var url = this.javaClass.classLoader.getResource("mybatis-config.xml");
        return url.openStream();
    }



    private fun acquireTruePath(path: String): String {
        if (System.getProperty("os.name").contains("Windows") &&
            (path.startsWith("/") || path.startsWith("\\"))
        ) {
            return path.substring(1, path.length)
        }
        return path;
    }


}