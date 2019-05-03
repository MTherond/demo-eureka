package com.example.demoeureka;

import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableEurekaServer
public class DemoEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoEurekaApplication.class, args);
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

		Runnable task = () -> {
			PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
			Applications applications = registry.getApplications();
			applications.getRegisteredApplications().forEach((registeredApplication) -> {
				registeredApplication.getInstances().forEach((instance) -> {
					System.out.println(instance.getAppName() + " (" + instance.getInstanceId() + ") : " + instance.getIPAddr());
				});
			});
		};

		//run this task after 5 seconds, nonblock for task3
		ses.scheduleAtFixedRate(task, 2, 1, TimeUnit.SECONDS);
	}

}
