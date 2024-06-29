package cn.aixcyi.plugin.bubbletodo.utils

import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory

object MyBatisUtil {

    private val sqlSessionFactory: SqlSessionFactory = SQLOperation.sqlSessionFactory


    fun <T> execute(mapperClass: Class<T>,function:(T) ->Unit){
        val session : SqlSession = sqlSessionFactory.openSession();
        try {
            val mapper: T = session.getMapper(mapperClass)
            function(mapper)
            session.commit()
        } finally {
            session.close()
        }
    }
}