#根据系统编码查询收费站列表
#sql("listStationBySystemCode")
 select * from tbl_paramSystemConfig where systemCode = #para(systemCode) and isActive = 1
#end

#查询所有的系统信息
#sql("queryAllSystem")
 select systemCode,systemName from tbl_paramSystemConfig where isActive = 1 group by systemCode,systemName
#end

#查询所有的系统信息
#sql("querySystemByCode")
 select systemCode,systemName from tbl_paramSystemConfig where isActive = 1 and systemCode = #para(systemCode) limit 1
#end

#查询系统的第一个收费站
#sql("queryFirstStationByCode")
 select * from tbl_paramSystemConfig where isActive = 1 and systemCode = #para(systemCode)  limit 1
#end