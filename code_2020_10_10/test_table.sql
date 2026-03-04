-- 检查表是否存在
SELECT COUNT(*) AS table_exists
FROM information_schema.tables
WHERE table_schema = 'jmeterboot'
  AND table_name = 'jmeter_perfor_current_report';

-- 查询表结构
DESCRIBE jmeter_perfor_current_report;

-- 查询最近的10条记录，按starttime降序排列
SELECT *
FROM jmeter_perfor_current_report
ORDER BY starttime DESC
LIMIT 10;

-- 查询特定starttime的数据（可替换为实际存在的starttime值）
-- 例如：SELECT * FROM jmeter_perfor_current_report WHERE starttime = '2023-01-01 00:00:00';