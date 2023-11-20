package com.andon.springbootutil.mapper;

import com.andon.springbootutil.entity.AuthorityUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Andon
 * 2023/11/20
 */
public interface DatabaseMapper {

    List<AuthorityUser> selectUser();

    void dropTable(@Param("tableName") String tableName);

    void createTable(@Param("tableName") String tableName, @Param("table") String table);

    void addColumn(@Param("tableName") String tableName, @Param("column") String column);

    void insertInto(@Param("tableName") String tableName, @Param("column") String column, @Param("values") String values);
}
