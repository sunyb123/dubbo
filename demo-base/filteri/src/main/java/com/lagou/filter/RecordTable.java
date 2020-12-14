package com.lagou.filter;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class RecordTable {

    /**
     * 总记录数
     */
    private static int count = 0;

    /**
     * 内部使用线程安全的 Google guava 包下的 Table 类型
     */
    private final Table<String, Integer, Integer> table = Tables.synchronizedTable(HashBasedTable.create());

    /**
     * 包含时间信息，记录为 1 秒
     */
    private LocalDateTime dateTime;

    public static RecordTable[] newArray() {
        count = 0;

        // 共计 60 等份
        int length = 60;
        final RecordTable[] array = new RecordTable[length];
        final LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 60; i++) {
            array[i] = new RecordTable();
            array[i].setDateTime(now);
        };
        return array;
    }

    public RecordTable() {}

    public Integer get(@Nullable Object rowKey, @Nullable Object columnKey) {
        return table.get(rowKey, columnKey);
    }

    public Integer put(String rowKey, Integer columnKey, Integer value) {
        count++;
        System.out.println("put 的次数 =" +  count);
        return table.put(rowKey, columnKey, value);
    }

    /**
     * 若 日期 和 时分秒 相等则认为是相等
     *
     * @param obj
     * @return
     */
    public boolean isDateTimeEquals(Object obj) {
        if (obj instanceof LocalDateTime) {
            LocalDateTime other = (LocalDateTime) obj;
            if (this.dateTime != null && this.dateTime.toLocalDate().equals(other.toLocalDate())) {
                final LocalTime localTime = this.dateTime.toLocalTime();
                final LocalTime otherLocalTime = other.toLocalTime();
                return localTime.getHour() == otherLocalTime.getHour()
                        && localTime.getMinute() == otherLocalTime.getMinute()
                        && localTime.getSecond() == otherLocalTime.getSecond();
            }
            return false;
        }
        return false;
    }

    public void clear() {
        table.clear();
    }

    public Map<String, Map<Integer, Integer>> rowMap() {
        return table.rowMap();
    }

    public boolean isEmpty(){
        return table.isEmpty();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
