-- sql主体模板
-- 引入MySQL脚本
#namespace("mysql")
 #include("mysql.sql")
#end

-- 引入Oracle脚本
#namespace("oracle")
 #include("oracle.sql")
#end

-- 引入SQL server脚本
#namespace("sqlserver")
 #include("sqlserver.sql")
#end