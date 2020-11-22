package cn.qinwh.mybatis.qservice.common;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @ClassName: BaseServiceImpl
 * @Description: 通用service实现类
 */
public class BaseServiceImpl<T> implements BaseService<T> {
    // 注入mapper
    @Autowired
    protected Mapper<T> mapper;

    // 缓存子类泛型类型
    private Class<T> cache=null;


    /**
     * @Description: 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     * @Date: 2019/6/9 21:57
     * @Param: id  主键id
     * @return: pojo单个实体对象
     **/
    @Override
    public T queryByPrimaryKey(Object key){
        return this.mapper.selectByPrimaryKey(key);
    }

    /**
     * @Description: 查询所有对象
     * @Date: 2019/6/9 21:59
     * @Param: 无
     * @return: java.util.List<T> list对象
     **/
    @Override
    public List<T> queryAll(){
        return this.mapper.select(null);
    }


    /**
     * @Description: 根据实体中的属性值进行查询，查询条件使用等号
     * @Date: 2019/6/9 22:00
     * @Param: param 条件
     * @return: java.util.List<T> 返回多条记录
     **/
    @Override
    public List<T> queryListByWhere(T pojo){
        return this.mapper.select(pojo);
    }


    /**
     * @Description: 根据实体中的属性查询总数，查询条件使用等号
     * @Date: 2019/6/9 22:02
     * @Param: pojo 实体对象
     * @return: 记录数
     **/
    @Override
    public int queryCount(T pojo){
        return this.mapper.selectCount(pojo);
    }


    /**
     * @Description: 查询一条记录
     *,通常条件是给一个实体对象，这个对象只有初始了一个id值(其它唯一值的字段也可以）
     *
     * @Date: 2019/6/9 22:03
     * @Param: pojo  条件对象,它的属性必须与表字段对应（可以用@Transient忽略字段），否则会报错，并且id必须唯一
     * @return: 返回单个pojo实体对象
     **/
    @Override
    public T queryOne(T pojo){
        return this.mapper.selectOne(pojo);
    }

    /**
     * 获取子类泛型类型
     * @return
     */
    @Override
    public Class<T> getTypeArguement() {
        if(cache==null){
            cache= (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return  cache;
    }

    /**
     * @Description: 插入一个pojo对象,null的属性也会保存，不会使用数据库默认值
     * @Date: 2019/6/9 22:05
     * @Param: pojo  实体对象
     * @return: 如果数值大于0则成功，null为失败
     **/
    @Override
    public int save(T pojo){
        return this.mapper.insert(pojo);
    }

    /**
     * @Description: 插入非空字段,null的属性不会保存，会使用数据库默认值
     *
     *
     * @Date: 2019/6/9 22:33
     * @Param: pojo 实体对象
     * @return: 如果数值大于0则成功，null为失败
     **/
    @Override
    public int saveSelect(T pojo){
        return this.mapper.insertSelective(pojo);
    }

    /**
     * @Description: 批量插入对象，非空字段,null的属性不会保存，会使用数据库默认值
     * @Date: 2019/6/10 0:01
     * @Param:  list 实体对象集合
     * @return: 如果数值大于0 则成功，null为失败
     **/
    @Override
    public int batchSave(List<T> list) {
        int result = 0;
        for (T record : list) {
            int count = mapper.insertSelective(record);
            result += count;
        }
        return result;
    }

    /**
     * @Description: 根据主键更新实体全部字段，null值会被更新
     * @Date: 2019/6/9 22:35
     * @Param: pojo  实体对象
     * @return: 如果数值大于0则成功，null为失败
     **/
    @Override
    public int update(T pojo){
        return this.mapper.updateByPrimaryKey(pojo);
    }

    /**
     *
     * @param pojo
     * @param o
     * @return
     */
    @Override
    public int updateByExample(T pojo, Object o){
        return this.mapper.updateByExample(pojo, o);
    }


    /**
     * @Description: 根据主键更新属性不为null的值
     * @Date: 2019/6/9 22:38
     * @Param: pojo 实体对象
     * @return:  如果数值大于0 则成功，null为失败
     **/
    @Override
    public int updateSelective(T pojo){
        return this.mapper.updateByPrimaryKeySelective(pojo);
    }

    /**
     *
     * @param pojo
     * @param o
     * @return
     */
    @Override
    public int updateByExampleSelective(T pojo, Object o){
        return this.mapper.updateByExampleSelective(pojo, o);
    }

    /**
     * @Description: 根据主键删除记录
     * @Date: 2019/6/9 22:39
     * @Param: id 主键id
     * @return: 删除的记录数量,如果数值大于0 则成功，null为失败
     **/
    @Override
    public int deleteByPrimaryKey(T key){
        return this.mapper.deleteByPrimaryKey(key);
    }

    /**
     * @Description: 根据主键的list集合,批量删除对象
     * @Date: 2019/6/9 22:41
     * @Param: clazz 实体对象
     * @Param: ids 主键的list集合
     * @return: 删除的记录数量,如果数值大于0 则成功，null为失败
     **/
    @Override
    public int deleteByIds(Class<T> clazz,List<Object> ids){
        // where条件
        Example example = new Example(clazz);
        example.createCriteria().andIn("id", ids);
        return this.mapper.deleteByExample(example);
        /*
           等效于where id in (#{ids})
         */
    }

    /**
     * @Description: 根据实体属性作为条件进行删除，查询条件使用等号
     * @Date: 2019/6/9 23:46
     * @Param: pojo 实体对象
     * @return: 删除的记录数量,如果数值大于0 则成功，null为失败
     **/
    @Override
    public int  deleteByWhere(T pojo){
        return this.mapper.delete(pojo);
    }

    /**
     * @Description: 批量删除对象，根据实体属性作为条件进行删除，查询条件使用等号
     * @Date: 2019/6/10 0:07
     * @Param:
     * @Param:
     * @return:
     **/
    @Override
    public int batchDelete(List<T> list) {
        int result = 0;
        for (T record : list) {
            int count = mapper.delete(record);
            if (count < 1) {
                //throw new BusinessException("删除数据失败!");
            }
            result += count;
        }
        return result;
    }

    /**
     * @Description: 根据Example条件进行删除
     * @Date: 2019/6/9 23:55
     * @Param:  example 查询条件对象
     * @return: 删除的记录数量,如果数值大于0 则成功，null为失败
     **/
    @Override
    public int deleteByExample(Object example){
        return this.mapper.deleteByPrimaryKey(example);
    }
}