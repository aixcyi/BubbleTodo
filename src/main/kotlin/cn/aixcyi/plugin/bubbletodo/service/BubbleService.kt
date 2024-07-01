package cn.aixcyi.plugin.bubbletodo.service

import cn.aixcyi.plugin.bubbletodo.entity.Bubble

interface BubbleService {


    fun findById(id: String): Bubble?;


    fun save(bubble: Bubble): String?;


    fun updateById(bubble: Bubble): Boolean;

    fun query(bubble:Bubble): List<Bubble>?
}