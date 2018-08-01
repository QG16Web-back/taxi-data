# Maven导入本地jar包

```
mvn install:install-file -Dfile=gson-2.2.4.jar -DgroupId=com.qg -DartifactId=gson -Dversion=2.2.4 -Dpackaging=jar

mvn install:install-file -Dfile=hadoop-auth-2.7.2.jar -DgroupId=com.qg -DartifactId=hadoop-auth -Dversion=2.7.2 -Dpackaging=jar

mvn install:install-file -Dfile=hadoop-common-2.7.2.jar -DgroupId=com.qg -DartifactId=hadoop-common -Dversion=2.7.2 -Dpackaging=jar

mvn install:install-file -Dfile=luna-0.1.jar -DgroupId=com.qg -DartifactId=luna -Dversion=0.1 -Dpackaging=jar

mvn install:install-file -Dfile=hbaseFileStream-1.0.jar -DgroupId=com.qg -DartifactId=hbase-file-stream -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=dynalogger-V100R002C30.jar -DgroupId=com.qg -DartifactId=dynalogger -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=hbase-secondaryindex-1.0.2.jar -DgroupId=com.qg -DartifactId=hbase-secondaryindex -Dversion=1.0.2 -Dpackaging=jar

mvn install:install-file -Dfile=phoenix-core-4.4.0-HBase-1.0.jar -DgroupId=com.qg -DartifactId=phoenix-core-4.4.0-HBase -Dversion=1.0 -Dpackaging=jar

mvn install:install-file -Dfile=zookeeper-3.5.1.jar -DgroupId=com.qg -DartifactId=zookeeper -Dversion=3.5.1 -Dpackaging=jar

mvn install:install-file -Dfile=hadoop-hdfs-2.7.2.jar -DgroupId=com.qg -DartifactId=hadoop-hdfs -Dversion=2.7.2 -Dpackaging=jar

mvn install:install-file -Dfile=hadoop-hdfs-client-2.7.2.jar -DgroupId=com.qg -DartifactId=hadoop-hdfs-client -Dversion=2.7.2 -Dpackaging=jar

mvn install:install-file -Dfile=hbase-client-1.0.2.jar -DgroupId=com.qg -DartifactId=hbase-client -Dversion=1.0.2 -Dpackaging=jar

mvn install:install-file -Dfile=hbase-common-1.0.2.jar -DgroupId=com.qg -DartifactId=hbase-common -Dversion=1.0.2 -Dpackaging=jar

mvn install:install-file -Dfile=hbase-protocol-1.0.2.jar -DgroupId=com.qg -DartifactId=hbase-protocol -Dversion=1.0.2 -Dpackaging=jar

mvn install:install-file -Dfile=hbase-server-1.0.2.jar -DgroupId=com.qg -DartifactId=hbase-server -Dversion=1.0.2 -Dpackaging=jar

mvn install:install-file -Dfile=htrace-core-3.1.0-incubating.jar -DgroupId=com.qg -DartifactId=htrace-core -Dversion=3.1.0-incubating -Dpackaging=jar
```

