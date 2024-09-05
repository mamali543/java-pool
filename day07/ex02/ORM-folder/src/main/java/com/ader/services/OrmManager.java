package com.ader.services;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        // ((HikariDataSource) dataSource).close();
    }


    private void setDatabase(Class<?> entity) throws SQLException{
        logger.info("we are ready to set our tables");
        logger.info(entity.getSimpleName());
        String sqlQuery = generateSetTableSqlString(entity);
        logger.info("<<CREATE table query>>>: "+sqlQuery);
        Connection connection = this.dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(sqlQuery);
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
                sql.append(getFieldType(f, ormColumn));
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


    private String getFieldType(Field f, OrmColumn ormColumn) throws SQLException {

        
        if (f.getType().equals(String.class))
        {
            int length = ormColumn.length();
            if (length <= 0)
                throw new SQLException("OrmColumn length must be greater than 0");
            return "VARCHAR(" + length + ")";
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

    public void save(Object entity) throws SQLException
    {
        logger.info("calling save methode: ");
        String sqlQuery = generateSaveSqlString(entity);
        logger.info("<<save query>>>: "+sqlQuery);
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sqlQuery,PreparedStatement.RETURN_GENERATED_KEYS);
        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next())
        {
            setNewId(entity, rs.getLong(1));
            try
            {
                Field id = entity.getClass().getDeclaredField("userId");
                id.setAccessible(true);
                Object v = id.get(entity);
                logger.info("<<New Inserted Id>>: "+ v);
            }
            catch(IllegalArgumentException | IllegalAccessException | NoSuchFieldException e)
            {
                throw new RuntimeException(e);
            }
            return ;
        }
        throw new SQLException("not saved");
    }

    private void setNewId(Object entity, long id) {

        Field[] fields = entity.getClass().getDeclaredFields();
        for(Field f: fields)
        {
            if (f.isAnnotationPresent(OrmColumnId.class))
            {
                f.setAccessible(true);
                try{
                    f.set(entity, id);
                }
                catch(IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private String generateSaveSqlString(Object entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity is null");
        Class<?> entityClass = entity.getClass();
        OrmEntity ormEntity = entityClass.getAnnotation(OrmEntity.class);
        if (ormEntity == null)
            throw new IllegalArgumentException("Entity is not an OrmEntity");
        String tableName = ormEntity.table();
        Field[] fields = entityClass.getDeclaredFields();
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName);
        sql.append(" (");
        for (Field f : fields)
        {
            if (f.isAnnotationPresent(OrmColumn.class))
            {
                OrmColumn ormColumn = f.getAnnotation(OrmColumn.class);
                String columnName = ormColumn.name();
                sql.append(columnName);
                sql.append(", ");
            }
        }
        sql.delete(sql.length() -2, sql.length());
        sql.append(") VALUES (");
        for (Field f: fields)
        {
            if (f.isAnnotationPresent(OrmColumn.class))
            {
                f.setAccessible(true);
                try
                {
                    Object value = f.get(entity);
                    if (value == null)
                        sql.append("NULL");
                    else if (isNumerique(f))
                        sql.append(value);
                    else
                        sql.append("'" + value + "'");
                    sql.append(", ");
                    
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
        sql.delete(sql.length() - 2, sql.length());
        sql.append(")");

        return sql.toString();
    }


    private boolean isNumerique(Field f) {
        return f.getType().equals(Long.class) ||f.getType().equals(long.class) 
        ||f.getType().equals(Integer.class) ||f.getType().equals(int.class) 
        ||f.getType().equals(Double.class) ||f.getType().equals(double.class);
    }

    public <T> Optional<T> findById(Long id, Class<T> aClass) throws SQLTimeoutException , SQLException{
        String sqlQuery = generateFindByIdSqlString(id, aClass);
        logger.info("<<SQLquery>>: "+ sqlQuery);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        if (resultSet.next())
        {
            // create an instance of entity user
            try{
                T entity = aClass.getDeclaredConstructor().newInstance();
                Field[] fields = entity.getClass().getDeclaredFields();

                for(Field f: fields)
                {
                    if (f.isAnnotationPresent(OrmColumn.class) || f.isAnnotationPresent(OrmColumnId.class))
                    {
                        f.setAccessible(true);
                        String columnName = f.getName();
                        if (f.isAnnotationPresent(OrmColumn.class))
                            columnName = f.getAnnotation(OrmColumn.class).name();
                        if (f.getType().equals(String.class))
                            f.set(entity, resultSet.getString(columnName));
                        else if (f.getType().equals(Long.class) || f.getType().equals(long.class))
                            f.set(entity, resultSet.getLong(columnName));
                        else if (f.getType().equals(double.class) || f.getType().equals(Double.class))
                            f.set(entity, resultSet.getDouble(columnName));
                        else if (f.getType().equals(Integer.class) || f.getType().equals(int.class))
                            f.set(entity, resultSet.getInt(columnName));
                        else if (f.getType().equals(Boolean.class) || f.getType().equals(boolean.class))
                            f.set(entity, resultSet.getBoolean(columnName));
                        else
                            throw new SQLException("OrmColumn type not supported");
                    }
                }
                return Optional.of(entity);
            }
            catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    private String generateFindByIdSqlString(Long id, Class<?> aClass) {
        if (id == null || id < 0)
            throw new IllegalArgumentException("Id is null or negative");
        if (aClass == null)
            throw new IllegalArgumentException("Class is null");
        OrmEntity ormEntity = aClass.getAnnotation(OrmEntity.class);
        if (ormEntity == null)
            throw new IllegalArgumentException("Class is not an OrmEntity");
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        String tableName = ormEntity.table();
        sql.append(tableName);
        sql.append(" WHERE ");
        for(Field f: aClass.getDeclaredFields())
        {
            if (f.isAnnotationPresent(OrmColumnId.class))
            {
                String columnName = f.getName();
                sql.append(columnName);
                sql.append(" = ");
                sql.append(id);
                sql.append(";");
            }
        }
        return sql.toString();
    }

    private DataSource createDataSourceConnection() {
        //set an object conf for a hickari datasource
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(DB_URL); // URL to your database
        config.setUsername(DB_USERNAME); // Database username

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        //construct the hickari dataSource with the conf object
        HikariDataSource hikariDataSource = new HikariDataSource(config);

        logger.info("Database succefully connected!");
        return hikariDataSource;
}
}