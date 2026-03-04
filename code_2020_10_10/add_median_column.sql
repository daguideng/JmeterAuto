-- 为jmeter_perfor_history_report表添加median字段
ALTER TABLE jmeter_perfor_history_report ADD COLUMN median VARCHAR(100) NOT NULL COMMENT 'median' AFTER max;