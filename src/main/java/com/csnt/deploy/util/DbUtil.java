package com.csnt.deploy.util;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.SqlServerDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author duwanjiang
 * @date 2018/3/19
 */
public class DbUtil {
    public enum OPERATE {
        EQUAL("="),
        LESS_THAN("<"),
        LESS_THAN_EQUAL("<="),
        GREATER_THAN_EQUAL(">="),
        NOT_EQUAL("<>"),
        IS_NULL("is null"),
        IS_NOT_NULL("is not null"),
        RIGHT_LIKE("like %"),
        LEFT_LIKE("%"),
        FULL_LIKE("%"),
        DEFAULT_LIKE("="),
        NOT_LIKE("="),
        IN("=");
        private String flag;

        OPERATE(String flag) {
            this.flag = flag;
        }

        public String get() {
            return flag;
        }
    }


    private static Logger logger = LoggerFactory.getLogger(DbUtil.class);

    /**
     * int[]数组求和
     */
    public static int sumResultCount(int[] result) {
        int resultSum = 0;
        for (int temp : result) {
            resultSum += temp;
        }
        return resultSum;
    }

    /**
     * 通过jdbc驱动名来获取数据库方言
     *
     * @param driverUrl
     * @return
     */
    public static Dialect getDbDialectByDriver(String driverUrl) {
        Dialect dialect = null;
        if (StringUtil.isEmpty(driverUrl)) {
            return dialect;
        }
        if (driverUrl.toLowerCase().indexOf("mysql") > -1) {
            dialect = new MysqlDialect();
        } else if (driverUrl.toLowerCase().indexOf("sqlserver") > -1) {
            dialect = new SqlServerDialect();
        } else if (driverUrl.toLowerCase().indexOf("oracle") > -1) {
            dialect = new OracleDialect();
        }
        return dialect;
    }


    /**
     * 根据sql模板获取sqlPara
     *
     * @param key
     * @param kv
     * @return
     */
    public static SqlPara getSqlPara(String key, Map kv) {
        return getSqlPara(key, kv, true);
    }

    /**
     * 根据sql模板获取sqlPara
     *
     * @param key
     * @param kv
     * @param isExistNameSpace
     * @return
     */
    public static SqlPara getSqlPara(String key, Map kv, boolean isExistNameSpace) {
        if (isExistNameSpace) {
            if (StringUtil.isEmpty(getCurrentDialectName())) {
                return null;
            }
            return Db.getSqlPara(getCurrentDialectName() + "." + key, kv);
        } else {
            return Db.getSqlPara(key, kv);
        }
    }

    /**
     * 获取模板中的sql语句
     *
     * @param key
     * @return
     */
    public static String getSql(String key) {
        return getSql(key, true);
    }

    /**
     * 获取模板sql语句
     *
     * @param key
     * @param isExistNameSpace
     * @return
     */
    public static String getSql(String key, boolean isExistNameSpace) {
        if (isExistNameSpace) {
            if (StringUtil.isEmpty(getCurrentDialectName())) {
                return null;
            }
            return Db.getSql(getCurrentDialectName() + "." + key);
        } else {
            return Db.getSql(key);
        }
    }

    /**
     * 获取模板sql语句
     *
     * @param key
     * @param param
     * @return
     */
    public static SqlPara getSqlParam(String key, Map param) {
        if (StringUtil.isEmpty(getCurrentDialectName())) {
            return null;
        }
        return Db.getSqlPara(getCurrentDialectName() + "." + key, param);

    }

    /**
     * 获取当前的方言名称
     *
     * @return
     */
    public static String getCurrentDialectName() {
        Dialect dialect = DbKit.getConfig().getDialect();
        if (dialect != null) {
            if (dialect instanceof MysqlDialect) {
                return "mysql";
            } else if (dialect instanceof OracleDialect) {
                return "oracle";
            } else if (dialect instanceof SqlServerDialect) {
                return "sqlserver";
            }
        }
        return null;
    }

    /**
     * 根据数据库方言获取数据的当前时间函数
     *
     * @return
     */
    public static String getDbCurrentDateSql() {
        String sql = "";
        //获取当前的方言对象
        Dialect dialect = DbKit.getConfig().getDialect();
        if (dialect != null) {
            if (dialect instanceof MysqlDialect) {
                sql = " SYSDATE() ";
            } else if (dialect instanceof OracleDialect) {
                sql = " sysdate ";
            } else if (dialect instanceof SqlServerDialect) {
                sql = " GETDATE() ";
            }
        }
        return sql;
    }

    /**
     * 获取主键uuid
     *
     * @return
     */
    public static String getUUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 判断错误是否是主键冲突
     *
     * @param e 错误异常
     * @return 是否是主键冲突
     */
    public static boolean isPrimarkKeyExpection(Exception e) {
        if (null != e.getMessage() && e.getMessage().contains("Unique constraint")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 拼接查询条件
     *
     * @param column    查询列
     * @param value     目标值
     * @param condition 条件支付查
     * @param params
     * @param <T>
     */
    public static <T> void initParam(String column, T value, StringBuilder condition, List<Object> params, OPERATE operator) {
        if (null != value) {
            if (operator == OPERATE.EQUAL) {
                condition.append(" and ").append(column).append(OPERATE.EQUAL.get()).append("? ");
                params.add(value);
            } else if (operator == OPERATE.LESS_THAN) {
                condition.append(" and ").append(column).append(OPERATE.LESS_THAN.get()).append("? ");
                params.add(value);
            } else if (operator == OPERATE.LESS_THAN_EQUAL) {
                condition.append(" and ").append(column).append(OPERATE.LESS_THAN_EQUAL.get()).append("? ");
                params.add(value);
            } else if (operator == OPERATE.GREATER_THAN_EQUAL) {
                condition.append(" and ").append(column).append(OPERATE.GREATER_THAN_EQUAL.get()).append("? ");
                params.add(value);
            } else if (operator == OPERATE.NOT_EQUAL) {
                condition.append(" and ").append(column).append(OPERATE.NOT_EQUAL.get()).append("? ");
                params.add(value);
            } else if (operator == OPERATE.IS_NULL) {
                condition.append(" and ").append(column).append(OPERATE.IS_NULL.get());
                params.add(value);
            } else if (operator == OPERATE.IS_NOT_NULL) {
                condition.append(" and ").append(column).append(OPERATE.IS_NOT_NULL.get());
                params.add(value);
            } else if (operator == OPERATE.RIGHT_LIKE) {
                condition.append(" and ").append(column).append(" like '%").append(value).append("'");
            } else if (operator == OPERATE.LEFT_LIKE) {
                condition.append(" and ").append(column).append(" like '").append(value).append("%'");
            } else if (operator == OPERATE.FULL_LIKE) {
                condition.append(" and ").append(column).append(" like '%").append(value).append("%'");
            } else if (operator == OPERATE.NOT_LIKE) {
                condition.append(" and ").append(column).append("not like ? ");
                params.add(value);
            } else if (operator == OPERATE.DEFAULT_LIKE) {
                condition.append(" and ").append(column).append(" like ? ");
                params.add(value);
            } else if (operator == OPERATE.IN) {
                condition.append(" and ").append(column).append(" like int (?) ");
            }
        }
    }

}
