package com.qg.taxi.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.security.User;

import java.io.IOException;

/**
 * @author Wilder Gao
 * time:2018/7/30
 * description：
 * motto: All efforts are not in vain
 */
public class Login {
    private static final String ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME = "Client";
    private static final String ZOOKEEPER_SERVER_PRINCIPAL_KEY = "zookeeper.server.principal";
    private static final String ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL = "zookeeper/hadoop";

    private static String krb5File = null;
    private static String userName = null;
    private static String userKeytabFile = null;

    public static void submitLogin(Configuration configuration) throws IOException {
        init(configuration);
        login(configuration);
    }

    private static void login(Configuration conf) throws IOException {
        if (User.isHBaseSecurityEnabled(conf)) {
            String userdir = System.getProperty("user.dir") + java.io.File.separator + "conf" + java.io.File.separator;

            userName = "leader_back2017";
            userKeytabFile = userdir + "user.keytab";
            krb5File = userdir + "krb5.conf";
            LoginUtil.setJaasConf(ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME, userName, userKeytabFile);
            LoginUtil.setZookeeperServerPrincipal(ZOOKEEPER_SERVER_PRINCIPAL_KEY,
                    ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL);
            LoginUtil.login(userName, userKeytabFile, krb5File, conf);
        }
    }

    private static void init(Configuration conf) throws IOException {
        String userdir = System.getProperty("user.dir") + java.io.File.separator + "conf" + java.io.File.separator;
        conf.addResource(new Path(userdir + "core-site.xml"));
        conf.addResource(new Path(userdir + "hdfs-site.xml"));
        conf.addResource(new Path(userdir + "hbase-site.xml"));
    }
}
