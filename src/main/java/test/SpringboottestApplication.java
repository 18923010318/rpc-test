package test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import server.NettyServer;

@SpringBootApplication
public class SpringboottestApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringboottestApplication.class, args);
        NettyServer nettyServer = new NettyServer();
        nettyServer.start();
    }

}
