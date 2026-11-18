package scu.cs.zhihu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("scu.cs.zhihu.mapper")
public class ZhihuKbApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZhihuKbApplication.class, args);
    }
}
