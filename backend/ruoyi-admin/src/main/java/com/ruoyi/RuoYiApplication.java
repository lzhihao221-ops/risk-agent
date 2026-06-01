package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

/**
 * 启动程序
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("\n" +
                "  ____  _     _     _                 \n" +
                " |  _ \\(_)___| | __| |                \n" +
                " | |_) | / __| |/ _` |                \n" +
                " |  _ <| \\__ \\ | (_| |                \n" +
                " |_| \\_\\_|___/_|\\__,_|                \n" +
                "  __  __           _ _                \n" +
                " |  \\/  | ___  _ _(_) |_ ___  _ _    \n" +
                " | |\\/| |/ -_)| '_| |  _/ -_)| ' \\   \n" +
                " |_|  |_|\\___||_| |_|\\__\\___||_||_|  \n" +
                " \n 风控管理系统启动成功！");
    }
}
