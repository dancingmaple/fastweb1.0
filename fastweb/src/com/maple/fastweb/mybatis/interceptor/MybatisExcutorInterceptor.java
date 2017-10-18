package com.maple.fastweb.mybatis.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Properties;


@Intercepts(value = {
		 @Signature(type = StatementHandler.class, method = "prepare", args = {
				Connection.class, Integer.class }),
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class, CacheKey.class, BoundSql.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class })
		/*,
		@Signature(type = StatementHandler.class, method = "prepare", args = {
				Connection.class,Integer.class})*/
		
		})
public class MybatisExcutorInterceptor implements Interceptor {
	private static final Logger logger = Logger
			.getLogger(MybatisExcutorInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (null == invocation){
			return null;
		}
		Object target = invocation.getTarget();
		Object result = null;
		System.out.println(target.getClass());
		if (invocation.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler)invocation.getTarget();
			BoundSql boundSql = statementHandler.getBoundSql();
			Object obj = boundSql.getParameterObject();
			String sql = boundSql.getSql();
			System.out.println("sql 语句为："+sql+"p="+obj);
			return invocation.proceed();

		}
		if (target instanceof Executor) {

			long start = System.currentTimeMillis();
			Method method = invocation.getMethod();
			/** 执行方法 */
			result = invocation.proceed();
			long end = System.currentTimeMillis();
			logger.info("[MybatisExcutorInterceptor] execute [" + method.getName()
					+ "] cost [" + (end - start) + "] ms");
		}



		logger.info("MybatisExcutorInterceptor -> intercept -> result="+result);
		return result;
	}

	@Override
	public Object plugin(Object target) {
		//logger.info("MybatisExcutorInterceptor -> plugin -> target="+target);
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		//logger.info("MybatisExcutorInterceptor -> setProperties -> properties="+properties);
	}

}
