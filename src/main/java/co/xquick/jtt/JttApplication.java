package co.xquick.jtt;

import co.xquick.jtt.jtt809.Jtt809NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * JTT
 *
 * @author Charles
 */
@SpringBootApplication()
public class JttApplication implements CommandLineRunner {

    @Autowired
    Jtt809NettyServer server;

    public static void main(String[] args) {
        SpringApplication.run(JttApplication.class, args);
    }

    @Override
    public void run(String... args) {
        server.start();
    }

}