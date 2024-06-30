package cn.aixcyi.plugin.bubbletodo.utils

import cn.aixcyi.plugin.bubbletodo.entity.Bubble
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.shelf.ShelveChangesManager
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import java.io.InputStream
import java.nio.charset.Charset
import java.sql.DriverManager


class SQLOperation : ShelveChangesManager.PostStartupActivity() {

    companion object {
        lateinit var sqlSessionFactory: SqlSessionFactory
        private val dbPath: String = "jdbc:sqlite:resources:bubbletodo.db"

    }

    /**
     * 启动时运行
     * 1. 运行数据库检查，如果数据库表不存在就新建表
     * 2. 新加sqlSessionFactory
     */
    override fun runActivity(project: Project) {
        super.runActivity(project)
        checkCreated()
        sqlSessionFactory = SqlSessionFactoryBuilder().build(acquireMybatisConfig())

    }

    private fun checkCreated() {
        Class.forName("org.sqlite.JDBC")
        var connection = DriverManager.getConnection(dbPath)
        var stmt = connection.createStatement()
        if (stmt.executeUpdate(Bubble.sql()) > 0) {
            stmt.executeUpdate(Bubble.createIndexSQL())
        }
        stmt.close()
        connection.close()
    }

    private fun acquireMybatisConfig(): InputStream {
        var url = this.javaClass.classLoader.getResource("mybatis-config.xml");
        var stream = url.openStream();
        return String(stream.readAllBytes()).replace("%{jdbc_link}%", dbPath)
            .byteInputStream(Charset.forName("utf-8"));
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