package com.dreamsecurity.sc;

import com.dreamsecurity.sc.config.SshSessionProperty;
import com.jcraft.jsch.JSchException;

public interface ISshConnection {
    void connect(SshSessionProperty property) throws JSchException;
    void sendAlive(String sessionName) throws Exception;
    boolean isConnect(String sessionName);
    void close(String sessionName);
}
