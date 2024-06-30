package cn.aixcyi.plugin.bubbletodo.mapper

import cn.aixcyi.plugin.bubbletodo.entity.Bubble
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select


interface BubbleMapper {


    fun selectById(@Param("id") id: String): Bubble;

    fun save(@Param("e") e: Bubble): Int

    fun updateById(@Param("e") e: Bubble): Int


    fun deleteById(@Param("id") id: String): Int

    fun query(@Param("e") e: Bubble): List<Bubble>
}