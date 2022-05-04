package com.dreamsecurity.sc.schedule;

import com.dreamsecurity.sc.SshConnection;
import com.dreamsecurity.sc.config.SshSessionProperties;
import com.dreamsecurity.sc.config.SshSessionProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Slf4j
@WebListener
public class SshContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("SSH Context initialized...");
        try {
            SshSessionProperties sshSessionProperties = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext()).getBean(SshSessionProperties.class);
            SshConnection sshConnection = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(SshConnection.class);

            for (SshSessionProperty sshSessionProperty : sshSessionProperties.getList()) {
                sshConnection.connect(sshSessionProperty);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("SSH Context Destroy!!");
        SshSessionProperties sshSessionProperties = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext()).getBean(SshSessionProperties.class);
        SshConnection sshConnection = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(SshConnection.class);

        for (SshSessionProperty sshSessionProperty : sshSessionProperties.getList()) {
            sshConnection.close(sshSessionProperty.getSessionName());
        }
    }
}
