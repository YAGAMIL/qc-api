package com.quantumtime.qc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Description: 项目启动入口
 * Created on 2019/10/10 17:31
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = "com.quantumtime.qc")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableJpaRepositories(basePackages = "com.quantumtime.qc.repository")
@EnableTransactionManagement
public class QcApplication {

	public static void main(String[] args) {
		SpringApplication.run(QcApplication.class, args);
	}


}
