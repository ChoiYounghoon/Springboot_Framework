package kr.co.application.config;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.BooleanTypeHandler;
import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class MybatisConfig {

	@Bean
	public SqlSessionFactory sqlSessionFactory (DataSource dataSource)throws Exception{
		SqlSessionFactoryBean sessionFactory=new SqlSessionFactoryBean();
		
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	  
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setTypeAliasesPackage("kr.co.repo.mapper");
		sessionFactory.setMapperLocations(resolver.getResources("classpath:mybatis/mappers/*.xml"));
		sessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
		sessionFactory.setTypeHandlers(new TypeHandler[] { new DateTypeHandler(), new BooleanTypeHandler()});
	 
		return sessionFactory.getObject();
	}
	 
	@Bean
	public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
 
}