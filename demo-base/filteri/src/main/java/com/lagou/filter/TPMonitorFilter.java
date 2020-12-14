package com.lagou.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Activate(group = {CommonConstants.CONSUMER,CommonConstants.PROVIDER})
public class TPMonitorFilter implements Filter {
    Schedule schedule = new Schedule();
    RecordTable[] recordArray = schedule.getRecordArray();



    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        long epochMilli = Instant.now().toEpochMilli();
        Result invoke = invoker.invoke(invocation);

        final LocalDateTime now = LocalDateTime.now();
        final Instant endInstant = now.atZone(ZoneId.systemDefault()).toInstant();
        // 方法耗时
        int costMills = (int) (endInstant.toEpochMilli() - epochMilli);
        // 方法名称
        String methodName = invocation.getMethodName();


        // localDateTime 对象包含了时分秒信息，取出秒 作为下标
        int index = now.getSecond();
        // 这里肯定不为 null, 若时间比对成功则则 进行 put
        if (recordArray[index].isDateTimeEquals(now)) {
            Integer value = recordArray[index].get(methodName, costMills);
            if (value != null) {
                recordArray[index].put(methodName, costMills, value + 1);
            } else {
                recordArray[index].put(methodName, costMills, 1);
            }
        } else {
            // 否则表示钟或小时至少有一个不存在则清空该 table 后重新put
            recordArray[index].clear();
            recordArray[index].setDateTime(now);
            // 秒不相等，判断分是否相等。 若相等。
            // 0~1 1_2 58~59 59~60(0)  一看到1分0秒，就表示完结了。   3分1秒    4分0秒       9分3秒 ~ 10分2秒
            // 0             59                                   181~182  240~241      543~544 ~ 602~603
            recordArray[index].put(methodName, costMills, 1);
        }

        return invoke;
    }
}
