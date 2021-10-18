package com.xiaohe66.crud.impl;

import com.xiaohe66.common.dto.Page;
import com.xiaohe66.crud.register.scan.CrudEntityWrapper;
import com.xiaohe66.crud.server.ICrudService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author xiaohe
 * @since 2021.09.27 15:10
 */
@Slf4j
public class DefaultCrudServiceImpl implements ICrudService<Map<String, Object>> {

    /*
    TODO : 这个类的实现传入的参数是一个 Map, 但实现上应该是实体类, 需要想办法纠正
     */

    private final CrudEntityWrapper entityWrapper;
    private final DataSource dataSource;

    private final String fields;

    private final String getByIdSql;
    private final String removeByIdSql;
    private final String listByParamSqlPrefix;
    private final String listByParamCountSqlPrefix;

    public DefaultCrudServiceImpl(CrudEntityWrapper entityWrapper, DataSource dataSource) {
        this.entityWrapper = entityWrapper;
        this.dataSource = dataSource;

        Set<String> fieldNames = entityWrapper.getFieldNames();

        if (fieldNames.isEmpty()) {
            throw new IllegalStateException("crud entity is not have field");
        }

        StringBuilder fieldsBuilder = new StringBuilder();
        for (String fieldName : fieldNames) {
            fieldsBuilder.append(',').append(fieldName);
        }

        this.fields = fieldsBuilder.substring(1);

        this.getByIdSql = String.format("select %s from %s where id = ?", fields, entityWrapper.getTableName());

        this.listByParamSqlPrefix = String.format("select %s from %s where 1=1 ", fields, entityWrapper.getTableName());
        this.listByParamCountSqlPrefix = String.format("select count(*) from %s where 1=1 ", entityWrapper.getTableName());

        // TODO : 逻辑删除
        this.removeByIdSql = String.format("delete from %s where id = ", entityWrapper.getTableName());

    }

    @Override
    public boolean add(Map<String, Object> params) {

        checkParams(params);

        StringBuilder fieldBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        Object[] paramArr = new Object[params.size()];
        int i = 0;

        for (Map.Entry<String, Object> entry : params.entrySet()) {

            fieldBuilder.append(',').append(entry.getKey());
            valueBuilder.append(",?");
            paramArr[i] = entry.getValue();

            i++;
        }

        String sql = String.format("insert into %s(%s) values(%s)",
                entityWrapper.getTableName(),
                fieldBuilder.substring(1),
                valueBuilder.substring(1));

        return execute(sql, paramArr);
    }

    @Override
    public boolean modifyById(Map<String, Object> params) {

        checkParams(params);

        if (!params.containsKey("id")) {
            throw new IllegalArgumentException("need field : id");
        }

        if (params.size() <= 1) {
            throw new IllegalArgumentException("not modify field");
        }

        Object[] paramArr = new Object[params.size()];

        Object id = params.remove("id");
        paramArr[paramArr.length - 1] = id;

        StringBuilder fieldBuilder = new StringBuilder();
        int i = 0;

        for (Map.Entry<String, Object> entry : params.entrySet()) {

            fieldBuilder.append(',').append(entry.getKey()).append("=?");
            paramArr[i] = entry.getValue();

            i++;
        }

        String sql = String.format("update %s set %s where id = ?",
                entityWrapper.getTableName(),
                fieldBuilder.substring(1));

        return execute(sql, paramArr);
    }

    @Override
    public Page<Map<String, Object>> listByParam(Map<String, Object> params, int pageNo, int pageSize) {

        Objects.requireNonNull(params);

        StringBuilder where = new StringBuilder();
        Object[] paramArr = new Object[params.size()];
        appendWhere(where, paramArr, params);

        String countSql = listByParamCountSqlPrefix + where.toString();
        log.debug("countSql : {}", countSql);

        try (Connection connection = dataSource.getConnection()) {

            long total;
            try (PreparedStatement countStatement = buildStatement(connection, countSql, paramArr);
                 ResultSet totalResultSet = countStatement.executeQuery()) {

                total = getFirstValueL(totalResultSet);
            }

            if (total <= 0) {
                return Page.emptyPage(pageNo, pageSize);
            }

            int start = (pageNo - 1) * pageSize;
            String querySql = String.format("%s %s limit %s,%s", listByParamSqlPrefix, where.toString(), start, pageSize);

            try (PreparedStatement queryStatement = buildStatement(connection, querySql, paramArr);
                 ResultSet resultSet = queryStatement.executeQuery()) {

                List<Map<String, Object>> list = getList(resultSet);

                Page.PageBuilder<Map<String, Object>> builder = Page.builder();

                return builder
                        .total(total)
                        .current(pageNo)
                        .size(pageSize)
                        .pages((total - 1) / pageSize + 1)
                        .records(list)
                        .build();
            }

        } catch (SQLException e) {

            log.error("getById error, sql : {}, params : {}", getByIdSql, params, e);
            throw new IllegalStateException(e);
        }
    }

    private void appendWhere(StringBuilder where, Object[] paramArr, Map<String, Object> params) {
        int i = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {

            where.append(" and ").append(entry.getKey()).append("=?");
            paramArr[i] = entry.getValue();

            i++;
        }
    }

    @Override
    public Map<String, Object> getById(Long id) {

        Objects.requireNonNull(id);

        // TODO : PreparedStatement 是否可复用
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getByIdSql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();

                if (!resultSet.next()) {
                    return null;
                }

                Map<String, Object> row = new HashMap<>();

                for (int i = 1; i <= count; ++i) {
                    String name = metaData.getColumnLabel(i);
                    Object oValue = resultSet.getObject(i);

                    row.put(name, oValue);
                }

                return row;
            }

        } catch (SQLException e) {

            log.error("getById error, sql : {}, id : {}", getByIdSql, id, e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean removeById(Long id) {

        Objects.requireNonNull(id);

        String sql = removeByIdSql + id;

        return execute(sql);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class getEntityCls() {
        // note : 默认实现类的 entity 就是 map
        // TODO : 需要修改逻辑，这里的 Map 就是 entity 会产生歧义
        return Map.class;
    }

    protected void checkParams(Map<String, Object> params) {
        if (params.isEmpty()) {
            throw new IllegalArgumentException("cannot empty");
        }
    }

    protected long insertReturnId(String sql, Object[] params) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setParams(statement, params);

            int changeQty = statement.executeUpdate(sql);

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    Object newIdObject = resultSet.getObject(1);

                    if (newIdObject != null) {
                        return NumberUtils.toLong(newIdObject.toString());
                    }
                }
            }

        } catch (SQLException e) {

            log.error("execute error, sql : {}, params : {}", sql, Arrays.toString(params), e);

        }
        return 0;
    }

    protected long getFirstValueL(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return 0;
        }

        Object object = resultSet.getObject(1);

        if (object == null) {
            return 0;
        }

        return NumberUtils.toLong(object.toString());
    }

    protected List<Map<String, Object>> getList(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();

        List<Map<String, Object>> list = new ArrayList<>();

        while (resultSet.next()) {

            Map<String, Object> row = new HashMap<>();

            for (int i = 1; i <= count; ++i) {
                String name = metaData.getColumnLabel(i);
                Object oValue = resultSet.getObject(i);

                row.put(name, oValue);
            }
            list.add(row);
        }

        return list;
    }

    protected boolean execute(String sql) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int changeQty = statement.executeUpdate(sql);
            return changeQty > 0;

        } catch (SQLException e) {

            log.error("execute error, sql : {}", sql, e);
            return false;
        }
    }

    protected boolean execute(String sql, Object[] params) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setParams(statement, params);

            int changeQty = statement.executeUpdate();
            return changeQty > 0;

        } catch (SQLException e) {

            log.error("execute error, sql : {}, params : {}", sql, Arrays.toString(params), e);
            return false;
        }
    }

    protected PreparedStatement buildStatement(Connection connection, String sql, Object[] params) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(sql);
        setParams(statement, params);
        return statement;
    }

    protected void setParams(PreparedStatement statement, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {

            // note : jdbc 设置参数从 1开始计数
            if (params[i] == null) {

                statement.setNull(i + 1, Types.VARCHAR);
            } else {

                statement.setObject(i + 1, params[i]);
            }
        }
    }


}
