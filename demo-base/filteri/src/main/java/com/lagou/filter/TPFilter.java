package com.lagou.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
@Activate(group = {CommonConstants.CONSUMER})
public class TPFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        System.out.println("-----------------------------------------------------------");
        return invoker.invoke(invocation);
    }
}
