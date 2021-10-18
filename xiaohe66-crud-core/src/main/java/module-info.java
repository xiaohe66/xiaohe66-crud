module xiaohe.crud.core {

    exports com.xiaohe66.crud;
    exports com.xiaohe66.crud.ex;
    exports com.xiaohe66.crud.register;
    exports com.xiaohe66.crud.register.scan;
    exports com.xiaohe66.crud.server;

    opens com.xiaohe66.crud;
    opens com.xiaohe66.crud.server;
    opens com.xiaohe66.crud.register;
    opens com.xiaohe66.crud.register.scan;

    requires transitive xiaohe.common.base;

    requires static lombok;

    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.core;
    requires spring.context;
    requires spring.web;

    requires transitive java.sql;

}