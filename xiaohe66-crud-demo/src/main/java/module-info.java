module xiaohe.crud.demo {

    // open 给 spring反射访问
    opens com.xiaohe66.crud.demo;
    opens com.xiaohe66.crud.demo.controller;
    opens com.xiaohe66.crud.demo.service;
    opens com.xiaohe66.crud.demo.mapper;
    opens com.xiaohe66.crud.demo.entity;
    opens com.xiaohe66.crud.demo.config;

    requires static lombok;

    requires xiaohe.crud.core;


    // 解决 mybatis报 Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required
    requires com.zaxxer.hikari;

    // 解决启动时闪退，则需要添加 tomcat
    requires org.apache.tomcat.embed.websocket;

    requires spring.boot.starter;
    requires spring.boot.starter.web;
    requires spring.boot.starter.tomcat;

    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;

    requires mybatis.plus.annotation;
    requires mybatis.plus.core;
    requires mybatis.plus.extension;
    requires org.mybatis.spring;

}