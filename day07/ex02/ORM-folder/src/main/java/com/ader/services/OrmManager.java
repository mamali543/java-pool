package com.ader.services;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ader.annotations.OrmColumn;
import com.ader.annotations.OrmColumnId;
import com.ader.annotations.OrmEntity;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class OrmManager {

    private DataSource dataSource;
    private List<String> registeredTables;
    private static final Logger logger = LoggerFactory.getLogger(OrmManager.class);
    final private String DB_URL="jdbc:postgresql://localhost:5432/postgres";
    final private String DB_USERNAME="m0saan";

    public OrmManager(Set<Class<?>> entities) throws SQLException{
        this.dataSource = createDataSourceConnection();
        this.registeredTables = new ArrayList<String>();
        for (Class<?> entity : entities)
            setDatabase(entity);
        ((HikariDataSource) dataSource).close();
    }


    private void setDatabase(Class<?> entity) throws SQLException{
        logger.info("we are ready to set our tables");
        logger.info(entity.getSimpleName());
        String sqlQuery = generateSetTableSqlString(entity);
        logger.info(sqlQuery);
        Connection connection = this.dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.executeQuery(sqlQuery);
    }

    private String generateSetTableSqlString(Class<?> entity) throws SQLException{
        OrmEntity ormEntity = entity.getAnnotation(OrmEntity.class);
        if (ormEntity == null) {
            throw new SQLException("Entity should not be null");
        }
        String tableName = ormEntity.table();
        if (tableName == null || tableName.isEmpty())
            throw new SQLException("Table name is empty");
        if (this.registeredTables.contains(tableName))
            throw new SQLException("Table already registerd");
        Field[] fields = entity.getDeclaredFields();
        StringBuilder sql = new StringBuilder("DROP TABLE IF EXISTS ");
        sql.append(tableName);
        sql.append(" CASCADE;");
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(tableName);
        sql.append(" (");
        boolean columnIdExist = false;
        for (Field f: fields)
        {
            if (f.isAnnotationPresent(OrmColumnId.class))
            {
                if (columnIdExist)
                    throw new SQLException("Error: Multiple @OrmColumnId annotations");
                if(!f.getType().equals(Long.class) && !f.getType().equals(long.class))
                    throw new SQLException("Error: Id Must Be of type Long or long");
                String columnName = f.getName();
                if (columnName == null || columnName.isEmpty())
                    throw new SQLException("Column name is empty");
                columnIdExist = true;
                sql.append(columnName);
                sql.append(" SERIAL NOT NULL  PRIMARY KEY, ");
            }
            else if (f.isAnnotationPresent(OrmColumn.class))
            {
                OrmColumn ormColumn = f.getAnnotation(OrmColumn.class);
                String columnName = ormColumn.name();
                if (columnName == null || columnName.isEmpty()) {
                    throw new SQLException("Column name is empty");
                }
                sql.append(columnName);
                sql.append(" ");
                sql.append(getFieldType(f, columnName));
                sql.append(", ");
            }
            if (!columnIdExist)
                throw new SQLException("No @OrmColumnId annotation");

        }
        sql.delete(sql.length() - 2, sql.length());
        sql.append(")");
        sql.append(";");
        this.registeredTables.add(tableName);
        return (sql.toString());
    }


    private String getFieldType(Field f, String columnName) throws SQLException {

        
        if (f.getType().equals(String.class))
        {
            int length = columnName.length() > 0 ? columnName.length() : 255;
            return "VARCHAR("+ length + ")";
        }
        else if (f.getType().equals(Long.class) || f.getType().equals(long.class)){
            return "BIGINT";
        }
        else if (f.getType().equals(Integer.class) || f.getType().equals(int.class)){
            return "INT";
        }
        else if (f.getType().equals(Double.class) || f.getType().equals(double.class)){
            return "DOUBLE";
        }
        else if (f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)){
            return "BOOLEAN";
        }
        else
            throw new SQLException("OrmColumn type not supported");
    }


    private DataSource createDataSourceConnection() {
        //set an object conf for a hickari datasource
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(DB_URL); // URL to your database
        config.setUsername(DB_USERNAME); // Database username

        //construct the hickari dataSource with the conf object
        HikariDataSource hikariDataSource = new HikariDataSource(config);

        logger.info("Database succefully connected!");
        return hikariDataSource;
}
}