package com.dreamsecurity.sc.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SshSessionProperty {
    /**
     * Session의 이름입니다. 특정 세션을 조회, 종료 등에 쓰입니다. 필수입니다.
     */
    private String sessionName;
    /**
     * 연결하고자 하는 호스트 명을 입력하면 됩니다.
     */
    private String connectionHost;
    /**
     * Port information required for Ssh connection.
     */
    private int connectionPort;
    /**
     * host ip address required for Ssh connection.
     */
    private String host;

    /**
     * ssh 연결에 대상 이름입니다.
     */
    private String userName;
    /**
     * ssh 연결에 필요한 패스워드입니다.
     */
    private String password;

    /**
     * ssh 연결에 사용할 임의의 사용자의 연결 포트입니다. 중복을 피해야합니다.
     *
     */
    private int lPort;
    /**
     * ssh 연결에 사용할 대상의 포트입니다.
     */
    private int rPort;
}
