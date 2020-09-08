package org.github.config;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.util.Date;


/**
 * @ClassName: MybatisPlusConfig
 * @Description:
 * @author: <p> 雅诗兰黛  熬夜不怕黑眼圈</p>
 * @date 2020-09-06 14:54
 * @Copyright: 墨铭琦妙代码研究中心
 */

@Slf4j
@Configuration
public class MybatisPlusConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public GlobalConfig globalConfig() {
        log.info("初始化GlobalConfiguration");
        GlobalConfig globalConfig = GlobalConfigUtils.defaults();
        globalConfig.setMetaObjectHandler(new MybatisPlusConfig.MetaData());
        globalConfig.setBanner(true);
        globalConfig.setSqlSessionFactory(sqlSessionFactory);
        globalConfig.setSuperMapperClass(BaseMapper.class);
        globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());
        GlobalConfig.DbConfig  dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setIdType(IdType.INPUT);
        dbConfig.setTablePrefix("t_");
        dbConfig.setLogicDeleteField("deleted");
        dbConfig.setLogicDeleteValue("1");
        dbConfig.setLogicNotDeleteValue("0");
        dbConfig.setSelectStrategy(FieldStrategy.NOT_EMPTY);
        dbConfig.setUpdateStrategy(FieldStrategy.NOT_EMPTY);
        dbConfig.setInsertStrategy(FieldStrategy.IGNORED);
        globalConfig.setDbConfig(dbConfig);
        log.info("mybatis plus 全局配置成功");
        return globalConfig;
    }


    class MetaData implements MetaObjectHandler{

        private  Date date = new Date();

        @Override
        public void insertFill(MetaObject metaObject) {
            metaObject.setValue("create_time", date);
            metaObject.setValue("update_time", date);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            metaObject.setValue("update_time", date);
        }
    }
}

