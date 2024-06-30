package cn.aixcyi.plugin.bubbletodo.service.impl

import cn.aixcyi.plugin.bubbletodo.entity.Bubble
import cn.aixcyi.plugin.bubbletodo.mapper.BubbleMapper
import cn.aixcyi.plugin.bubbletodo.service.BubbleService
import cn.aixcyi.plugin.bubbletodo.utils.MyBatisUtil

import com.intellij.openapi.components.Service

@Service(value = [Service.Level.APP])
internal class BubbleServiceImpl : BubbleService {


    override fun findById(id: String): Bubble? {
        var bubble: Bubble? = null;
        MyBatisUtil.execute(BubbleMapper::class.java) { mapper ->
            bubble = mapper.selectById(id);
        }
        return bubble;
    }


    override fun save(bubble: Bubble): String? {
        var id: String? = null;
        MyBatisUtil.execute(BubbleMapper::class.java) { mapper ->
            mapper.save(bubble)
            id = bubble.id
        }
        return id;
    }


    override fun updateById(bubble: Bubble): Boolean {
        var result: Boolean = false
        MyBatisUtil.execute(BubbleMapper::class.java) { mapper ->
            result = mapper.updateById(bubble) >= 1;
        }
        return result;
    }


    override fun query(bubble:Bubble): List<Bubble>?{
        var result:List<Bubble>? =null
        MyBatisUtil.execute(BubbleMapper::class.java) { mapper ->
            result = mapper.query(bubble);
        }
       return result
    }

}