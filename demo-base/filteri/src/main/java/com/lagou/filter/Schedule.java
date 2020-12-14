package com.lagou.filter;

import java.util.*;

public class Schedule {
    private  Timer timer;
    RecordTable[] recordArray = new RecordTable[60];

    public Schedule() {
        for (int i=0;i<60;i++){
            recordArray[i] = new RecordTable();
        }

        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 每隔 5s 打印一次最近1分钟内每个方法的TP90、TP99的耗时情况
                System.out.println("----------每5s一次的任务开始----------");

                // 总行数（最大值为 length）
                int totalColumnSize = 0;
                // 总记录数(执行方法的总次数)
                int totalSize = 0;
                int aTotalSize = 0;
                int bTotalSize = 0;
                int cTotalSize = 0;

                List<Integer> listA = new ArrayList<>(800);
                List<Integer> listB = new ArrayList<>(800);
                List<Integer> listC = new ArrayList<>(800);

                for (int i = 0; i < recordArray.length; i++) {
                    if (recordArray[i] == null || recordArray[i].isEmpty()) {
                        continue;
                    }

                    // 行记录数 = (方法A+B+C调用的总次数)
                    int size = 0;
                    // 每行方法A的调用次数
                    int methodAtotalSize = 0;
                    int methodBtotalSize = 0;
                    int methodCtotalSize = 0;

                    final Map<String, Map<Integer, Integer>> rowMap = recordArray[i].rowMap();
                    for (Map.Entry<String, Map<Integer, Integer>> entry : rowMap.entrySet()) {
                        final String methodName = entry.getKey();

                        final Map<Integer, Integer> value = entry.getValue();
                        if (value != null) {
                            for (Map.Entry<Integer, Integer> valueEntry : value.entrySet()) {
                                // 消耗毫秒数
                                Integer costMills = valueEntry.getKey();
                                // 消费次数
                                final Integer count = valueEntry.getValue();
                                size += count;

                                if ("methodA".equalsIgnoreCase(methodName)) {
                                    methodAtotalSize += count;
                                    for (int j = 0; j < count; j++) {
                                        listA.add(costMills);
                                    }
                                }
                                else if ("methodB".equalsIgnoreCase(methodName)) {
                                    methodBtotalSize += count;
                                    for (int j = 0; j < count; j++) {
                                        listB.add(costMills);
                                    }
                                }
                                else if ("methodC".equalsIgnoreCase(methodName)) {
                                    methodCtotalSize += count;
                                    for (int j = 0; j < count; j++) {
                                        listC.add(costMills);
                                    }
                                }
                            }

                            if ("methodA".equalsIgnoreCase(methodName)) {
                                aTotalSize += methodAtotalSize;
                            } else if ("methodB".equalsIgnoreCase(methodName)) {
                                bTotalSize += methodBtotalSize;
                            } else if ("methodC".equalsIgnoreCase(methodName)) {
                                cTotalSize += methodCtotalSize;
                            }
                        }
                    }
                    totalColumnSize++;
                    totalSize += size;

                    System.out.println("行记录数" + size
                            + "其中A记录数 = " + methodAtotalSize
                            + ", B记录数 = " + methodBtotalSize
                            + ", C记录数 = " + methodCtotalSize
                            + ", 下标为" + i
                            + "， 时间为: " + recordArray[i].getDateTime()
                            + ", " + recordArray[i].rowMap());
                }
                System.out.println("总记录数 = " + totalSize + "("+ aTotalSize +", " + bTotalSize  +", " + cTotalSize +")"
                        + ", 总行数 = " + totalColumnSize + "\n------每5s一次的任务结束-----");

                // A的 top 90
                int aTop90 = (int) Math.ceil(aTotalSize * 0.9D) -1;
                int aTop99 = (int) Math.ceil(aTotalSize * 0.99D) - 1;
                // 升序取第 %90 位 或 逆序取第 %10
                System.out.println("top A T90下标=" + aTop90 + ", T99 下标=" + aTop99);

                Object[] a = listA.toArray();
//                Arrays.sort(a, (Comparator) new TPComparator());
                System.out.println(Arrays.toString(a));
                if(a.length!=0){
                    System.out.println("top A T90 =" + a[aTop90] + ", T99 =" + a[aTop99] + "\n---");
                }


                // B的 top 90
                int bTop90 = (int) Math.ceil(bTotalSize * 90D / 100) - 1;
                int bTop99 = (int) Math.ceil(bTotalSize * 99D / 100) - 1;
                System.out.println("top B T90下标=" + bTop90 + ", T99下标=" + bTop99);
                Object[] b = listB.toArray();
//                Arrays.sort(b, (Comparator) new TPComparator());
                System.out.println(Arrays.toString(b));
                if(b.length!=0){
                    System.out.println("top B T90 =" + b[bTop90] + ", T99 =" + b[bTop99] + "\n---");
                }


                // C的 top 90
                int cTop90 = (int) Math.ceil(cTotalSize * 90D / 100) - 1;
                int cTop99 = (int) Math.ceil(cTotalSize * 99D / 100) - 1;
                System.out.println("top C T90下标=" + cTop90 + ", T99下标=" + cTop99);
                Object[] c = listC.toArray();
//                Arrays.sort(c, (Comparator) new TPComparator());
                System.out.println(Arrays.toString(c));
                if(c.length!=0){
                    System.out.println("top C T90 =" + c[cTop90] + ", T99 =" + c[cTop99] + "\n---");
                }

            }
        },0,5*1000);
    }

    public RecordTable[] getRecordArray() {

        return recordArray;
    }
}

